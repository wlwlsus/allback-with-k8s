package com.allback.cygipayment.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduleTask {

    private final JobLauncher jobLauncher;
    private final Job myJob;
    static int num = 0;

//    @Scheduled(cron = "0 0 0 * * ?") // 매일 자정에 실행
    @Scheduled(cron = "*/20 * * * * *")
    public void runMyJob() throws JobExecutionException {
        JobExecution jobExecution = jobLauncher.run(myJob, new JobParameters());
        BatchStatus status = jobExecution.getStatus();
        System.out.println("********************************************");
        System.out.println(status + " " + num++);
        System.out.println("********************************************");
        // Job 실행 결과에 따른 처리 로직 추가
    }
}
