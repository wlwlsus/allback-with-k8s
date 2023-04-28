package com.allback.cygipayment.batch;

import com.allback.cygipayment.entity.Reservation;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterProcess;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.FlowJobBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.StepContext;
import org.springframework.batch.core.scope.context.StepSynchronizationManager;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableBatchProcessing
public class MyJobConfig {

    private final DataSource dataSource;



    @Bean
    public Job myJob(JobRepository jobRepository, Step myStep1, Step myStep2) {
        log.info("this is job");
        return new JobBuilder("myJob", jobRepository)
            .start(myStep1)
//            .on("STOPPED").end()
            .next(myStep2)
            .build();

    }

    @Bean
    public Step myStep1(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        log.info("this is step1");
        //우선 공연이 마감된 공연 id를 가져와야함
//        HashSet<Long> concertList = concertServerClient.getEndedConcert().getBody();
        HashSet<Long> concertList = null;
        //넘겨온 값이 없으면 mysep1를 끝낸다
        if (concertList == null || concertList.isEmpty()) {
            log.info("concertList is empty, stopping job...");
            return new StepBuilder("myStep1", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    return RepeatStatus.FINISHED;
                }, transactionManager)
                .build();
        }
        //concertList를 myStep2에서 사용하도록 함
        ExecutionContext stepExecutionContext = new ExecutionContext();
        stepExecutionContext.put("concertList", concertList);

        LocalDate today = LocalDate.now();
        JobParameters jobParameters = new JobParametersBuilder()
            .addDate("date", Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant()))
            .toJobParameters();

        StepExecution stepExecution = jobRepository.getLastStepExecution(jobRepository.getLastJobExecution("myJob", jobParameters).getJobInstance(), "myStep1");
        stepExecution.setExecutionContext(stepExecutionContext);
        return new StepBuilder("myStep1", jobRepository)
            .tasklet((contribution, chunkContext) -> {
                return RepeatStatus.FINISHED;
            })
            .build();
    }

    @Bean
    public Step myStep2(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        log.info("this is step2");
        return new StepBuilder("myStep1", jobRepository)
            .<Reservation, Map<Long, Integer>>chunk(10, transactionManager)
            .reader(reservationReader())
            .processor(reservationProcessor()) // 수정된 UserCountProcessor 사용
            .writer(balanceWriter()) // balanceWriter 사용
            .build();

    }

    @Bean
    @StepScope
    public JdbcPagingItemReader<Reservation> reservationReader() {
        // ExecutionContext에서 concertList를 가져와 사용
        StepContext executionContext = StepSynchronizationManager.getContext();
        HashSet<Long> concertList = (HashSet<Long>) executionContext.getAttribute("concertList");

        // reservation을 읽기 위한 JdbcPagingItemReader 생성
        JdbcPagingItemReader<Reservation> reader = new JdbcPagingItemReader<>();
        reader.setDataSource(dataSource);
        reader.setPageSize(10);
        reader.setRowMapper(new BeanPropertyRowMapper<>(Reservation.class));
        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();

        // concertList의 값을 사용해 where 절을 동적으로 생성합니다.
        StringBuilder whereClauseBuilder = new StringBuilder("WHERE status = '예약완료' AND concert_id IN (");
        for (Long concertId : concertList) {
            whereClauseBuilder.append(concertId).append(",");
        }
        whereClauseBuilder.deleteCharAt(whereClauseBuilder.length() - 1).append(")");
        queryProvider.setSelectClause("id, user_id, concert_id, status, price");
        queryProvider.setFromClause("FROM reservation");
        queryProvider.setWhereClause(whereClauseBuilder.toString());
        queryProvider.setSortKeys(Collections.singletonMap("id", Order.ASCENDING));
        reader.setQueryProvider(queryProvider);
        return reader;
    }
    @Bean
    @StepScope
    public ItemProcessor<Reservation, Map<Long, Integer>> reservationProcessor() {
        return new ItemProcessor<Reservation, Map<Long, Integer>>() {
            private final Map<Long, Integer> userCountMap = new HashMap<>();
            @Override
            public Map<Long, Integer> process(Reservation item) throws Exception {
                userCountMap.compute(item.getUserId(), (k, v) -> (v == null) ? 1 : v + 1);
                return null;
            }
            @AfterProcess
            public void afterProcess() {
                log.info("User count map: {}", userCountMap);
                ExecutionContext stepExecutionContext = new ExecutionContext();
                stepExecutionContext.put("userCountMap", userCountMap); // userCountMap을 ExecutionContext에 저장
            }
        };
    }


    @Bean
    @StepScope
    public ItemWriter<Map<Long, Integer>> balanceWriter() {
        JdbcBatchItemWriter<Map<Long, Integer>> writer = new JdbcBatchItemWriter<>();
        writer.setDataSource(dataSource);
        writer.setSql("INSERT INTO balance (user_id, amount) VALUES (?, ?)");
        writer.setItemPreparedStatementSetter(new ItemPreparedStatementSetter<Map<Long, Integer>>() {
            @Override
            public void setValues(Map<Long, Integer> item, PreparedStatement ps) throws SQLException {
                for (Map.Entry<Long, Integer> entry : item.entrySet()) {
                    ps.setLong(1, entry.getKey());
                    ps.setInt(2, entry.getValue());
                    ps.addBatch();
                }
            }
        });
        return writer;
    }

}
