package com.example.springbatchguide.step.tasklet;

import com.example.springbatchguide.listener.JobLoggerListener;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import com.example.springbatchguide.step.CustomService2;

/**
 * 파라미터와 함께 사용하기
 * */
//@EnableBatchProcessing
//@Configuration
@RequiredArgsConstructor
public class MethodInvokingTaskletAdapterV2 {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job methodInvokingJob1() {
        //
        return this.jobBuilderFactory.get("methodInvokingJob1")
                .start(methodInvokingStep1())
                .listener(new JobLoggerListener())
                .build();
    }

    @Bean
    public Step methodInvokingStep1() {
        //
        return this.stepBuilderFactory.get("methodInvokingStep1")
                .tasklet(methodInvokingTasklet1(null))
                .build();
    }

    @Bean
    public org.springframework.batch.core.step.tasklet.MethodInvokingTaskletAdapter methodInvokingTasklet1(
            @Value("#{jobParameters['name']}") String message
    ) {
        //
        org.springframework.batch.core.step.tasklet.MethodInvokingTaskletAdapter methodInvokingTaskletAdapter =
                new org.springframework.batch.core.step.tasklet.MethodInvokingTaskletAdapter();

        methodInvokingTaskletAdapter.setTargetObject(service1());
        methodInvokingTaskletAdapter.setTargetMethod("serviceMethod");
        methodInvokingTaskletAdapter.setArguments(new String[]{message});
        return methodInvokingTaskletAdapter;
    }

    @Bean
    public CustomServiceV2 service1() {
        //
        return new CustomServiceV2();
    }


}
