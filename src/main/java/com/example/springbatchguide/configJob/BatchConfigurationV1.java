package com.example.springbatchguide.configJob;

import com.example.springbatchguide.executioncontext.GoodByeTasklet;
import com.example.springbatchguide.executioncontext.HelloTasklet;
import com.example.springbatchguide.listener.JobLoggerListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.listener.ExecutionContextPromotionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableBatchProcessing
@Configuration
public class BatchConfigurationV1 {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job job1() {
        return this.jobBuilderFactory.get("job")
                .start(step1_1())
                .next(step2_1())
                .incrementer(new RunIdIncrementer())
                .listener(new JobLoggerListener())
                .build();
    }

    @Bean
    public Step step1_1() {
        return this.stepBuilderFactory.get("step1")
                .tasklet(new HelloWorldJob()) // 잡을 통한 ExecutionContext 접근이 되지 않는다. 아래의 과정을 거쳐야 가능해짐
                .tasklet(new HelloTasklet())
                .listener(promotionListener())
                .build();
    }

    @Bean
    public Step step2_1() {
        return this.stepBuilderFactory.get("step2")
                .tasklet(new GoodByeTasklet())
                .tasklet(new HelloWorldJob())
                .build();
    }

    @Bean // 스텝1이 끝나면 스텝의 ExecutionContext 를 다음 스텝에서 쓸 수 있도록 잡의 ExecutionContext 로 승격시키기
    public StepExecutionListener promotionListener() {
        ExecutionContextPromotionListener listener = new ExecutionContextPromotionListener();

        listener.setKeys(new String[]{"name"});
        return listener;
    }

    public static class HelloWorldJob implements Tasklet {
        private static final String HELLO_WORLD = "Hello, %s";
        @Override
        public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                //
                String name = (String) chunkContext.getStepContext()
                        .getJobParameters()
                        .get("name");

                // 잡의 ExecutionContext 를 가져오는 방법
        ExecutionContext jobContext = chunkContext.getStepContext()
                .getStepExecution()
                .getJobExecution()
                .getExecutionContext();

                jobContext.put("user.name", name);

                System.out.println(String.format(HELLO_WORLD, name));
                return RepeatStatus.FINISHED;

        }
    }


}
