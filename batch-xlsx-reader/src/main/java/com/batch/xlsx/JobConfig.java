package com.batch.xlsx;

import com.batch.xlsx.step.RecordProcessor;
import com.batch.xlsx.step.RecordWriter;
import com.batch.xlsx.step.mapper.FieldMapper;
import com.batch.xlsx.step.ExcelItemReader;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.File;


@Configuration
public class JobConfig {

    public static void main(String[] args) {
        SpringApplication.run(JobConfig.class, args);
    }

    @Bean
    public Job importSuperExcelJob(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) throws Exception {
        return jobBuilderFactory.get("jobLauncher")
                .incrementer(new RunIdIncrementer())
                .listener(listener())
                .flow(importSuperExcepStep(stepBuilderFactory))
                .end()
                .build();

    }

    private Step importSuperExcepStep(StepBuilderFactory stepBuilderFactory) throws Exception {
        return stepBuilderFactory.get("importSuperExcelStep")
                .<String, String>chunk(500)
                .reader(superReader(null))
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    @StepScope
    public ExcelItemReader<String> superReader(@Value("#{jobParameters[filePath]}") String filePath) throws Exception {

        ExcelItemReader<String> reader = new ExcelItemReader<>(
                new FileSystemResource(filePath), 1, new FieldMapper()
        );

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
