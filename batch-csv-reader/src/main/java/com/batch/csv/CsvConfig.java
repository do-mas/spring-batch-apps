package com.batch.csv;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;


@Configuration
public class CsvConfig {

    public static void main(String[] args) {
        SpringApplication.run(CsvConfig.class, args);
    }

    @Bean
    public Job importCsvJob(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) throws Exception {
        return jobBuilderFactory.get("jobLauncher")
                .incrementer(new RunIdIncrementer())
                .listener(listener())
                .flow(importInvoiceStep(stepBuilderFactory))
                .end()
                .build();

    }

    private Step importInvoiceStep(StepBuilderFactory stepBuilderFactory) throws Exception {
        return stepBuilderFactory.get("importCsvStep")
                .chunk(500)
                .reader(csvReader(null))
                .processor(processor())
                .writer(writer())
                .build();
    }


    @Bean
    @StepScope
    public FileReader csvReader(@Value("#{jobParameters[filePath]}") String filePath) throws Exception {
        FileReader fileReader = new FileReader();
        CsvFieldMapper mapper = new CsvFieldMapper();

        URL url = getClass().getClassLoader().getResource("test.csv");
        File fileToImport = new File(url.getFile());
        fileToImport.createNewFile();


        DefaultLineMapper defaultLineMapper = new DefaultLineMapper();
        defaultLineMapper.setFieldSetMapper(mapper);
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames(getHeader(fileToImport));
        defaultLineMapper.setLineTokenizer(tokenizer);

        fileReader.setLineMapper(defaultLineMapper);
        fileReader.setLinesToSkip(1);

        fileReader.setResource(new FileSystemResource(fileToImport));
        return fileReader;
    }


    private String[] getHeader(File file) throws IOException {
        CSVParser parser = new CSVParser(new java.io.FileReader(file), CSVFormat.DEFAULT.withHeader());
        Map<String, Integer> header = parser.getHeaderMap();
        String[] headerNames = new String[header.size()];
        header.forEach((k, val) -> headerNames[val] = k);
        return headerNames;
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
