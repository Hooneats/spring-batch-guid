package com.example.springbatchguide.timestemper;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersIncrementer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DailyJobTimestamper implements JobParametersIncrementer {
    @Override
    public JobParameters getNext(JobParameters jobParameters) {
        return new JobParametersBuilder(jobParameters)
//                .addDate("currentDate", Date.from(LocalDate.now().atStartOfDay(ZoneId.of("Asia/Seoul")).toInstant()))
//                .addDate("currentDate", Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()))
//                .addDate("currentDate", Date.valueOf(LocalDate.now()))
                .addDate("currentDate", new Date())
                .toJobParameters();
    }

}
