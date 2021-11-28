package com.example.springbatchguide.step.tasklet;

import com.example.springbatchguide.listener.JobLoggerListener;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableBatchProcessing
@Configuration
@RequiredArgsConstructor
public class MethodInvokingTaskletAdapter {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job methodInvokingJob() {
        //
        return this.jobBuilderFactory.get("methodInvokingJob")
                .start(methodInvokingStep())
                .listener(new JobLoggerListener())
                .build();
    }

    @Bean
    public Step methodInvokingStep() {
        //
        return this.stepBuilderFactory.get("methodInvokingStep")
                .tasklet(methodInvokingTasklet())
                .build();
    }

    /**
     * 파라미터를 결합해 사용도 가능하다. -> MethodInvokingTaskletAdapter.java
     * */
    @Bean
    public org.springframework.batch.core.step.tasklet.MethodInvokingTaskletAdapter methodInvokingTasklet() {
        //
        org.springframework.batch.core.step.tasklet.MethodInvokingTaskletAdapter methodInvokingTaskletAdapter =
                new org.springframework.batch.core.step.tasklet.MethodInvokingTaskletAdapter();

        methodInvokingTaskletAdapter.setTargetObject(service());
        methodInvokingTaskletAdapter.setTargetMethod("serviceMethod");
        return methodInvokingTaskletAdapter;
    }

    @Bean
    public CustomService service() {
        //
        return new CustomService();
    }


}
