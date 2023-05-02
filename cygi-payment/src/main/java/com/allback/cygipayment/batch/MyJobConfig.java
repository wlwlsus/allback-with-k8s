package com.allback.cygipayment.batch;

import com.allback.cygipayment.client.ConcertServerClient;
import com.allback.cygipayment.client.UserServerClient;
import com.allback.cygipayment.entity.Balance;
import com.allback.cygipayment.entity.BalanceState;
import com.allback.cygipayment.entity.Reservation;
import com.allback.cygipayment.repository.BalanceRepository;
import com.allback.cygipayment.repository.BalanceStateRepository;
import jakarta.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableBatchProcessing
public class MyJobConfig {

    private final BalanceRepository balanceRepository;
    private final BalanceStateRepository balanceStateRepository;
    private final EntityManagerFactory entityManagerFactory;
    private final ConcertServerClient concertServerClient;
    private final UserServerClient userServerClient;
    private int chunkSize = 10;
    private List<Long> concertList = new ArrayList<>();
    private class MyStep1Listener implements StepExecutionListener {
        @Override
        public void beforeStep(StepExecution stepExecution) {
            // Nothing to do here
        }
        @Override
        public ExitStatus afterStep(StepExecution stepExecution) {
            stepExecution.getExecutionContext().put("concertList", concertList);
            return ExitStatus.COMPLETED;
        }
    }

    @Bean
    public Job myJob(JobRepository jobRepository, Step myStep1, Step myStep2, Step myStep3) {
        log.info("this is job");
        return new JobBuilder("myJob", jobRepository)
            .start(myStep1)  // concertId 목록 가져오기
            .next(myStep2)  // reservation -> balance
            .next(myStep3)  //balace -> balanceState
            .build();
    }

    @Bean
    public Step myStep1(JobRepository jobRepository,
        PlatformTransactionManager transactionManager) {
        return new StepBuilder("myStep1", jobRepository)
            .tasklet((contribution, chunkContext) -> {
                //오늘마감이 콘서트id리스트 불러오기
                concertList = concertServerClient.getEndedConcert().getBody();
                System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                System.out.println(concertList);
                System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                if(concertList.size()==0 || concertList.isEmpty()) {
                    throw new RuntimeException("Concert list is empty");
                }
                chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().put("concertIds", concertList); // concertIds를 ExecutionContext에 저장
                return RepeatStatus.FINISHED;
            }, transactionManager)
            .listener(new MyStep1Listener())
            .allowStartIfComplete(true)
            .build();
    }

    @Bean
    public Step myStep2(JobRepository jobRepository,
        PlatformTransactionManager transactionManager) {
        return new StepBuilder("myStep2", jobRepository)
            .<Reservation, Balance>chunk(chunkSize, transactionManager)
            .reader(reservationReader(concertList))
            .processor(reservationProcessor()) // 수정된 UserCountProcessor 사용
            .writer(balanceWriter()) // balanceWriter 사용
            .allowStartIfComplete(true)
            .build();

    }

    @Bean
    @StepScope
    public JpaPagingItemReader<Reservation> reservationReader(@Value("#{jobExecutionContext['concertList']}") List<Long> concertList) {
        return new JpaPagingItemReaderBuilder<Reservation>()
            .name("reservationReader")
            .entityManagerFactory(entityManagerFactory)
            .pageSize(chunkSize)
            .queryString(
                "SELECT r FROM Reservation r WHERE r.status like '예약완료' AND r.concertId IN :concertIds")
            .parameterValues(Collections.singletonMap("concertIds", concertList))
            .build();
    }

    @Bean
    public ItemProcessor<Reservation, Balance> reservationProcessor() {
        return reservation -> {
            return new Balance(reservation.getReservationId(), reservation.getConcertId(),
                reservation.getUserId(), reservation.getPrice(), reservation.getSeat());
        };
    }

    @Bean
    public JpaItemWriter<Balance> balanceWriter() {
        JpaItemWriter<Balance> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
        return jpaItemWriter;
    }

    @Bean
    public Step myStep3(JobRepository jobRepository,
        PlatformTransactionManager transactionManager) {
        return new StepBuilder("myStep3", jobRepository)
            .tasklet((contribution, chunkContext) -> {
                for (int i = 0; i < concertList.size(); i++) {
                    Map<String, Object> state = balanceRepository.getBalanceByConcertId(concertList.get(i));
                    //state는 total, cnt
                    if(state.containsKey("total")) {
                        BalanceState balanceState = BalanceState.builder()
                            .proceed((Long) state.get("total"))
                            .customer((Long) state.get("cnt"))
                            .concertId((Long)state.get("concertId"))
                            .userId(1L)
                            .build();
                        balanceStateRepository.save(balanceState);
                        //주최자에거 돈 주면 됨
                        Long receiverId = concertServerClient.getUserId(concertList.get(i)).getBody();
                        Long point = (Long) state.get("total");
                        userServerClient.sendPoint(receiverId, point);
                    }
                }
                return RepeatStatus.FINISHED;
            }, transactionManager)
            .allowStartIfComplete(true)
            .build();
    }


}

