package com.example.springbatchguide.validate;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;

public class ParameterValidator implements JobParametersValidator {
    @Override
    public void validate(JobParameters jobParameters) throws JobParametersInvalidException {
        String fileName = jobParameters.getString("fileName");

        if (!StringUtils.hasText(fileName)) { // fileName 파라미터 값이 없거나
            throw new JobParametersInvalidException("fileName parameter is missing");
        } else if (!StringUtils.endsWithIgnoreCase(fileName, "csv")) { // fileName 의 값이 csv 로 끝나지 않으면 예외가 발생한다.
            throw new JobParametersInvalidException("fileName parameter does not use th csv file extension");
        }
    }

    @Bean
    public JobParametersValidator validator() {
        DefaultJobParametersValidator validator = new DefaultJobParametersValidator();

        validator.setRequiredKeys(new String[]{"fileName"});
        validator.setOptionalKeys(new String[]{"name"});

        return validator;
    }
}
