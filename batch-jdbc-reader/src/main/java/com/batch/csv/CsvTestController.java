package com.batch.csv;

import com.batch.FileUtils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("csv")
public class CsvTestController {


    @Autowired
    @Qualifier("simpleJobLauncher")
    private SimpleJobLauncher simpleJobLauncher;

    @Autowired
    @Qualifier("importCsvJob")
    private Job importCsvJob;

    @GetMapping("test")
    public String started() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, IOException {

        ClassPathResource resource = new ClassPathResource("test.csv");
        File file = new File(resource.getURL().getFile());

        JobParameters jobParameters =
                new JobParametersBuilder()
                        .addString("job-id", UUID.randomUUID().toString())
                        .addString("filePath", file.getPath()).toJobParameters();

        simpleJobLauncher.run(importCsvJob, jobParameters);

        return "finished";

    }


    @PostMapping("upload")
    public String validateFile(@RequestParam("file") MultipartFile multipart) throws Exception {

        File file = FileUtils.convertToFile(multipart);
        file.createNewFile();

        JobParameters jobParameters =
                new JobParametersBuilder()
                        .addLong("time", System.currentTimeMillis())
                        .addString("filePath", file.getPath()).toJobParameters();

        simpleJobLauncher.run(importCsvJob, jobParameters);

        return "Done";

    }

}
