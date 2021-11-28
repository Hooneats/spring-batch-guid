//package com.example.springbatchguide.step;
//
//import com.example.springbatchguide.listener.JobLoggerListener;
//import lombok.RequiredArgsConstructor;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
//import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
//import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
//import org.springframework.batch.core.step.tasklet.SimpleSystemProcessExitCodeMapper;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.task.SimpleAsyncTaskExecutor;
//
//@EnableBatchProcessing
//@Configuration
//@RequiredArgsConstructor
//public class AdvancedCommandJob {
//
//    private final JobBuilderFactory jobBuilderFactory;
//    private final StepBuilderFactory stepBuilderFactory;
//
//    @Bean
//    public Job advancedCommandJob() {
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
//
//        systemCommandTasklet.setWorkingDirectory("/Users/mminella/spring-batch");
//
//        systemCommandTasklet.setSystemProcessExitCodeMapper(touchCodeMapper());
//        systemCommandTasklet.setTerminationCheckInterval(5000);
//        systemCommandTasklet.setTaskExecutor(new SimpleAsyncTaskExecutor());
//        systemCommandTasklet.setEnvironmentParams(new String[]{
//                "JAVA_HOME=/java",
//                "BATCH_HOME=/Users/batch"
//        });
//        return systemCommandTasklet;
//    }
//
//    @Bean
//    public SimpleSystemProcessExitCodeMapper touchCodeMapper() {
//        return new SimpleSystemProcessExitCodeMapper();
//    }
//
//}
