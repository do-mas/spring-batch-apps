package com.batch.csv;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;

public class JobListener extends JobExecutionListenerSupport {

    public JobListener() {
    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        System.out.println("before job");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        System.out.println("after job");
    }

}
