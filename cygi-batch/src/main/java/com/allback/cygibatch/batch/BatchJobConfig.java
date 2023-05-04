package com.allback.cygibatch.batch;

import com.allback.cygibatch.client.ConcertServerClient;
import com.allback.cygibatch.client.UserServerClient;
import com.allback.cygibatch.entity.Balance;
import com.allback.cygibatch.entity.BalanceState;
import com.allback.cygibatch.entity.Reservation;
import com.allback.cygibatch.repository.BalanceRepository;
import com.allback.cygibatch.repository.BalanceStateRepository;
import jakarta.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableBatchProcessing
public class BatchJobConfig {

    private final BalanceRepository balanceRepository;
    private final BalanceStateRepository balanceStateRepository;
    private final EntityManagerFactory entityManagerFactory;
    private final ConcertServerClient concertServerClient;
    private final UserServerClient userServerClient;
    private int chunkSize = 10;
    private static List<Long> concertList;
    private class MyStep1Listener implements StepExecutionListener {
        @Override
        public void beforeStep(StepExecution stepExecution) {
            // Nothing to do here
        }
        @Override
        public ExitStatus afterStep(StepExecution stepExecution) {
//            stepExecution.getExecutionContext().put("concertList", concertList);
            stepExecution.getJobExecution().getExecutionContext().put("concertList", concertList);
            return ExitStatus.COMPLETED;
        }
    }

    @Bean
    @Primary
    public Job batchJob(JobRepository jobRepository, Step getConcertListStep, Step balanceStep, Step balanceStateStep) {
        return new JobBuilder("batchJob", jobRepository)
            .start(getConcertListStep)  // concertId 목록 가져오기
                .on("STOPPED")
                .end()
            .from(getConcertListStep)
                .on("*")
                .to(balanceStep)  // reservation -> balance
                .next(balanceStateStep)  //balace -> balanceState
                .on("FAILED")
                .fail()
                .on("*")
                .end()
            .end()
            .build();
//        return new JobBuilder("batchJob", jobRepository)
//            .start(getConcertListStep)  // concertId 목록 가져오기
//            .next(balanceStep)  // reservation -> balance
//            .next(balanceStateStep)  //balace -> balanceState
//            .build();
    }

    @Bean
    public Step getConcertListStep(JobRepository jobRepository,
        PlatformTransactionManager transactionManager) {
        return new StepBuilder("getConcertListStep", jobRepository)
            .tasklet((contribution, chunkContext) -> {
                System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                JobExecution jobExecution = contribution.getStepExecution().getJobExecution();
                System.out.println("jobExecution = " + jobExecution);
                JobInstance jobInstance = contribution.getStepExecution().getJobExecution().getJobInstance();
                System.out.println("jobInstance.getId() : " + jobInstance.getId());
                System.out.println("jobInstance.getInstanceId() : " + jobInstance.getInstanceId());
                System.out.println("jobInstance.getJobName() : " + jobInstance.getJobName());
                System.out.println("jobInstance.getJobVersion : " + jobInstance.getVersion());
                JobParameters jobParameters = contribution.getStepExecution().getJobParameters();
                System.out.println(jobParameters.getString("name"));
                //오늘마감이 콘서트id리스트 불러오기
                concertList = concertServerClient.getEndedConcert().getBody();
                System.out.println(concertList);
                if(concertList.size()==0 || concertList.isEmpty()) {
                    contribution.setExitStatus(ExitStatus.STOPPED);
                    return RepeatStatus.FINISHED;
                }
                return RepeatStatus.FINISHED;
            }, transactionManager)
            .listener(new MyStep1Listener())
            .allowStartIfComplete(true)
            .build();
    }

    @Bean
    public Step balanceStep(JobRepository jobRepository,
        PlatformTransactionManager transactionManager, List<Long> concertList) {
        return new StepBuilder("balanceStep", jobRepository)
            .<Reservation, Balance>chunk(chunkSize, transactionManager)
            .reader(reservationReader(concertList))
            .processor(reservationProcessor()) // 수정된 UserCountProcessor 사용
            .writer(balanceWriter()) // balanceWriter 사용
            .allowStartIfComplete(true)
            .build();

    }

    @Bean
//    @StepScope
    @JobScope
    public JpaPagingItemReader<Reservation> reservationReader(@Value("#{jobExecutionContext['concertList']}") List<Long> concertList) {
        return new JpaPagingItemReaderBuilder<Reservation>()
            .name("reservationReader")
            .entityManagerFactory(entityManagerFactory)
            .pageSize(chunkSize)
            .queryString(
                "SELECT r FROM Reservation r WHERE r.status like '예약완료' AND r.concertId IN :concertList")
            .parameterValues(Collections.singletonMap("concertList", concertList))
            .build();
    }

    @Bean
    public ItemProcessor<Reservation, Balance> reservationProcessor() {
        return reservation -> {
            reservation.setStatus("정산완료");
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
    public Step balanceStateStep(JobRepository jobRepository,
        PlatformTransactionManager transactionManager) {
        return new StepBuilder("balanceStateStep", jobRepository)
            .tasklet((contribution, chunkContext) -> {
                System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                System.out.println("balanceStateStep 배치 실행됨");
                System.out.println(concertList);
                System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                for (int i = 0; i < concertList.size(); i++) {
                    Map<String, Object> state = balanceRepository.getBalanceByConcertId(concertList.get(i));
                    //state는 total, cnt
                    if(state.containsKey("total")) {
                        BalanceState balanceState = BalanceState.builder()
                            .proceed((long) state.get("total"))
                            .customer((long) state.get("cnt"))
                            .concertId((long)state.get("concertId"))
                            .userId(1L)
                            .build();
                        balanceStateRepository.save(balanceState);
                        //주최자에거 돈 주면 됨
                        long receiverId = concertServerClient.getUserId(concertList.get(i)).getBody();
                        long point = (long) state.get("total");
                        userServerClient.sendPoint(receiverId, point);
                    }
                }
                return RepeatStatus.FINISHED;
            }, transactionManager)
            .allowStartIfComplete(true)
            .build();
    }


}

