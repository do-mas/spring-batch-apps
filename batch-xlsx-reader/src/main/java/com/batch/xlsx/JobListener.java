package com.batch.xlsx;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;

public class JobListener extends JobExecutionListenerSupport {

    public JobListener() {
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        System.out.println("Job listener: Before job");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        System.out.println("Job listener: After job");
    }

}
