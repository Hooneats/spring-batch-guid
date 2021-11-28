package com.example.springbatchguide.executioncontext;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;

public class HelloWorld implements Tasklet {
    private static final String HELLO_WORLD = "Hello, %s";

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
        //
        String name = (String) chunkContext.getStepContext()
                .getJobParameters()
                .get("name");

        // 잡의 ExecutionContext 를 가져오는 방법
//        ExecutionContext jobContext = chunkContext.getStepContext()
//                .getStepExecution()
//                .getJobExecution()
//                .getExecutionContext();

        // 스텝의 ExecutionContext 를 가져와 조작하는 방식도  .getJobExecution() 를 제회하면된다.
        ExecutionContext jobContext = chunkContext.getStepContext()
                .getStepExecution()
                .getExecutionContext();

        jobContext.put("user.name", name);

        System.out.println(String.format(HELLO_WORLD, name));
        return RepeatStatus.FINISHED;
    }
}
