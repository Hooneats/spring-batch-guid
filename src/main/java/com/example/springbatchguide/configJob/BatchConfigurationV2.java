package com.example.springbatchguide.configJob;

import com.example.springbatchguide.executioncontext.GoodByeTasklet;
import com.example.springbatchguide.executioncontext.HelloTasklet;
import com.example.springbatchguide.executioncontext.HelloWorld;
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
public class BatchConfigurationV2 {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job job2() {
        return this.jobBuilderFactory.get("job")
                .start(step1_2())
                .incrementer(new RunIdIncrementer())
                .listener(new JobLoggerListener())
                .build();
    }

    @Bean
    public Step step1_2() {
        return this.stepBuilderFactory.get("step1")
                .tasklet(HelloWorldTasklet())
                .build();
    }

    private Tasklet HelloWorldTasklet() {
        return new Helloworld1();
    }

    public static class Helloworld1 implements Tasklet{
        private static final String HELLO_WORLD = "Hello, %s";

        @Override
        public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
            //
            String name = (String) chunkContext.getStepContext()
                    .getJobParameters()
                    .get("name");

            ExecutionContext jobContext = chunkContext.getStepContext()
                    .getStepExecution()
                    .getExecutionContext();

            jobContext.put("user.name", name);

            System.out.println(String.format(HELLO_WORLD, name));
            return RepeatStatus.FINISHED;
        }
    }
}
