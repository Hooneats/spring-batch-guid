//package com.example.springbatchguide.step;
//
//import com.example.springbatchguide.listener.JobLoggerListener;
//import lombok.RequiredArgsConstructor;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
//import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
//import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
//import org.springframework.batch.core.step.tasklet.Tasklet;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@EnableBatchProcessing
//@Configuration
//@RequiredArgsConstructor
//public class SystemCommandTasklet {
//
//    private final JobBuilderFactory jobBuilderFactory;
//    private final StepBuilderFactory stepBuilderFactory;
//
//    @Bean
//    public Job systemCommandJob() {
//        return this.jobBuilderFactory.get("systemCommandJob")
//                .start(systemCommandStep())
//                .listener(new JobLoggerListener())
//                .build();
//    }
//
//    @Bean
//    public Step systemCommandStep() {
//        return this.stepBuilderFactory.get("systemCommandStep")
//                .tasklet(systemCommandTasklet())
//                .build();
//    }
//
//    @Bean
//    public org.springframework.batch.core.step.tasklet.SystemCommandTasklet systemCommandTasklet() {
//        org.springframework.batch.core.step.tasklet.SystemCommandTasklet systemCommandTasklet =
//                new org.springframework.batch.core.step.tasklet.SystemCommandTasklet();
//        systemCommandTasklet.setCommand("rm -rf /tmp.txt");
//        systemCommandTasklet.setTimeout(5000);
//        systemCommandTasklet.setInterruptOnCancel(true);
//        return systemCommandTasklet;
//    }
//
//}
