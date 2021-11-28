package com.example.springbatchguide;

import com.example.springbatchguide.listener.JobLoggerListener;
import com.example.springbatchguide.timestemper.DailyJobTimestamper;
import com.example.springbatchguide.validate.ParameterValidator;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.CompositeJobParametersValidator;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.listener.JobListenerFactoryBean;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;


@EnableBatchProcessing
@SpringBootApplication
public class SpringBatchGuideApplication {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public CompositeJobParametersValidator validator() {
        CompositeJobParametersValidator validator = new CompositeJobParametersValidator();

        DefaultJobParametersValidator defaultJobParametersValidator = new DefaultJobParametersValidator(
                new String[]{"fileName"},
//                new String[]{"name", "run.id"} // RunIdIncrementer 사용시
                new String[]{"name", "currentDate"} // DailyJobTimestamper 사용시
        );

        defaultJobParametersValidator.afterPropertiesSet();

        validator.setValidators(
                Arrays.asList(new ParameterValidator(), defaultJobParametersValidator)
        );

        return validator;
    }

    @Bean
    public Step step1() {
        //
        return this.stepBuilderFactory.get("step1")
                .tasklet(TaskletHelloWorldTasklet(null, null)).build();
        /** Tasklet 을 구성하는 방법은 람다식을 이용해서도 할 수 있다. */
    }

    @Bean
    public Tasklet helloWorldTasklet() { // ChunkContext 방식의 파라미터 접근
        return (stepContribution, chunkContext) -> {
            String name = (String) chunkContext.getStepContext()
                    .getJobParameters()
                    .get("name");
            System.out.println(String.format("Hello, %s!", name));
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    @StepScope
    public Tasklet TaskletHelloWorldTasklet( // 늦은 바인딩 방식의 파라미터 접근
            @Value("#{jobParameters['name']}") String name,
            @Value("#{jobParameters['fileName']}") String fileName
    ) {
        return (stepContribution, chunkContext) -> {
            System.out.println(String.format("Hello, %s!", name));
            System.out.println(String.format("fileNmae, %s!", fileName));
            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Job job() {
        //
        return this.jobBuilderFactory.get("basicJob")
                .start(step1())
                .validator(validator())
//                .incrementer(new RunIdIncrementer()) // RunIdIncrementer 사용시
                .incrementer(new DailyJobTimestamper()) // DailyJobTimestamper 사용시
//                .listener(new JobLoggerListener())
                .listener(JobListenerFactoryBean.getListener(new JobLoggerListener()))
                .build();
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringBatchGuideApplication.class, args);
    }

}
