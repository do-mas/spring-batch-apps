package com.batch.superxlsx;

import com.batch.csv.JobListener;
import exc.SuperExcelFieldMapper;
import exc.SuperExcelItemReader;
import exc.SuperRowMapper;
import exc.orig.ExcelItemReader;
import exc.orig.RowMapper;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;


@Configuration
public class SuperExcelConfig {

    public static void main(String[] args) {
        SpringApplication.run(SuperExcelConfig.class, args);
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
                .chunk(500)
                .reader(superReader(null))
                .processor(processor())
                .writer(writer())
                .build();
    }

    @SuppressWarnings("all")
    @Bean
    @StepScope
    public SuperExcelItemReader<String> superReader(@Value("#{jobParameters[filePath]}") String filePath) throws Exception {

        SuperExcelItemReader<String> reader = new SuperExcelItemReader(
                new ClassPathResource("test.xlsx"), 1, new SuperExcelFieldMapper()
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
    public ItemWriter<String> writer() {
        return new RecordWriter();
    }

    @Bean
    @JobScope
    public JobExecutionListener listener() {
        return new JobListener();
    }




}
