package com.batch.xlsx;

import exc.orig.ExcelItemReader;
import exc.orig.RowMapper;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;


@Configuration
public class ExcelConfig {

    public static void main(String[] args) {
        SpringApplication.run(ExcelConfig.class, args);
    }

    @Bean
    public Job importExcelJob(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) throws Exception {
        return jobBuilderFactory.get("jobLauncher")
                .incrementer(new RunIdIncrementer())
                .listener(listener())
                .flow(importInvoiceStep(stepBuilderFactory))
                .end()
                .build();

    }

    private Step importInvoiceStep(StepBuilderFactory stepBuilderFactory) throws Exception {
        return stepBuilderFactory.get("importExcelStep")
                .chunk(500)
                .reader(reader(null))
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    @StepScope
    public ExcelItemReader reader(@Value("#{jobParameters[filePath]}") String filePath) throws Exception {

        ExcelItemReader<String> reader = new ExcelItemReader<>();
        reader.setResource(new ClassPathResource("test.xlsx"));
        reader.setLinesToSkip(1);
        RowMapper<String> rm = new ExcelFieldMapper();
        reader.setRowMapper(rm);
        return reader;

    }

    @Bean
    @StepScope
    public ItemProcessor processor() {
        return new RecordProcessor();
    }

    @Bean
    @StepScope
    public RecordWriter writer() {
        return new RecordWriter();
    }

    @Bean
    @JobScope
    public JobExecutionListener listener() {
        return new JobListener();
    }




}
