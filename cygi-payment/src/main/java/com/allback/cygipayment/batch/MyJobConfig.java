package com.allback.cygipayment.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
@Slf4j
@Configuration
public class MyJobConfig extends DefaultBatchConfiguration {
    @Bean
    public Job myJob(JobRepository jobRepository, Step myStep1, Step myStep2) {
        log.info("this is job");
        return new JobBuilder("myJob", jobRepository)
            .start(myStep1)
            .next(myStep2)
            .build();
    }

    @Bean
    public Step myStep1(JobRepository jobRepository, Tasklet myTasklet1, PlatformTransactionManager transactionManager) {
        log.info("this is step1");
        return new StepBuilder("myStep1", jobRepository)
            .tasklet(myTasklet1, transactionManager)
            .build();
    }

    @Bean
    public Step myStep2(JobRepository jobRepository, Tasklet myTasklet2, PlatformTransactionManager transactionManager) {
        log.info("this is step2");
        return new StepBuilder("myStep2", jobRepository)
            .tasklet(myTasklet2, transactionManager)
            .build();
    }

    @Bean
    public Tasklet myTasklet1() {
        System.out.println(
            """
                    this is myTasklet1
                    """
        );
        // Step에서는 Tasklet을 무한 반복 시킨다. 그래서 RepeatStatus을 null || RepeatStatus.FINISHED로 주어야 한번 실행하고 끝난다.
        return new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                System.out.println(
                    """
                            myTasklet1
                            ==============================================
                            >> contribution = %s
                            >> chunkContext = %s
                            ==============================================
                            """.formatted(contribution, chunkContext)
                );
                return RepeatStatus.FINISHED;
            }
        };
    }

    @Bean
    public Tasklet myTasklet2() {
        System.out.println("this is taskLet2");
        // Step에서는 Tasklet을 무한 반복 시킨다. 그래서 RepeatStatus을 null || RepeatStatus.FINISHED로 주어야 한번 실행하고 끝난다.
        return (contribution, chunkContext) -> {
            System.out.println("test2");
            System.out.println(
                """
                        myTasklet2
                        ==============================================
                        >> contribution = %s
                        >> chunkContext = %s
                        ==============================================
                        """.formatted(contribution, chunkContext)
            );
            return RepeatStatus.FINISHED;
        };
    }
}
