package com.allback.gateway.config;

import com.allback.gateway.KafkaService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

@Configuration
@EnableScheduling
public class SchedulerConfig {

    @Autowired
    private KafkaService kafkaService; // 요청 처리를 위한 서비스 클래스를 주입합니다.

    // JobDetail을 생성하는 메서드입니다.
    @Bean
    public JobDetail yourJobDetail() {
        return JobBuilder.newJob(YourJob.class)
                .withIdentity("yourJob") // Job 식별자를 설정합니다.
                .storeDurably() // Job이 실행되지 않아도 유지되도록 설정합니다.
                .build();
    }

    // Trigger를 생성하는 메서드입니다.
    @Bean
    public Trigger yourTrigger() {
        return TriggerBuilder.newTrigger()
                .forJob(yourJobDetail()) // 해당 Trigger가 실행될 Job을 설정합니다.
                .withIdentity("yourTrigger") // Trigger 식별자를 설정합니다.
                .withSchedule(CronScheduleBuilder.cronSchedule("0/10 * * ? * *")) // 10초마다 실행하도록 스케줄링합니다.
                .build();
    }

    // Scheduler를 생성하는 메서드입니다.
    @Bean
    public Scheduler yourScheduler() throws SchedulerException {
        Scheduler scheduler = new org.quartz.impl.StdSchedulerFactory().getScheduler(); // 스케줄러 인스턴스를 생성합니다.
        scheduler.setJobFactory(springBeanJobFactory()); // Spring Bean을 Job으로 사용하기 위해 JobFactory를 설정합니다.
        scheduler.scheduleJob(yourJobDetail(), yourTrigger()); // Job과 Trigger를 스케줄러에 등록합니다.
        scheduler.start(); // 스케줄러를 시작합니다.
        return scheduler;
    }

    // Spring Bean을 Job으로 사용하기 위한 JobFactory를 생성하는 메서드입니다.
    @Bean
    public SpringBeanJobFactory springBeanJobFactory() {
        return new SpringBeanJobFactory();
    }

    // 실제 작업을 수행하는 Job 클래스입니다.
    public class YourJob implements org.quartz.Job {
        @Override
        public void execute(JobExecutionContext context) {
            kafkaService.schedulerProcess();
        }
    }
}
