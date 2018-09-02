package com.batch.superxlsx;


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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
public class SuperExcelTestController {


    @Autowired
    @Qualifier("simpleJobLauncher")
    private SimpleJobLauncher simpleJobLauncher;

    @Autowired
    @Qualifier("importSuperExcelJob")
    private Job importExcelJob;


    @GetMapping("super/test")
    public String started() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, IOException {

        ClassPathResource resource = new ClassPathResource("test.xlsx");
        File file = new File(resource.getURL().getFile());

        JobParameters jobParameters =
                new JobParametersBuilder()
                        .addLong("job-time", System.currentTimeMillis())
                        .addString("filePath", file.getPath()).toJobParameters();

        simpleJobLauncher.run(importExcelJob, jobParameters);

        return "upload finished";

    }


    @PostMapping("super/upload")
    public String validateFile(@RequestParam("file") MultipartFile multipart) throws Exception {

        File file = FileUtils.convertToFile(multipart);
        file.createNewFile();

        JobParameters jobParameters =
                new JobParametersBuilder()
                        .addLong("job-time", System.currentTimeMillis())
                        .addString("filePath", file.getPath()).toJobParameters();

        simpleJobLauncher.run(importExcelJob, jobParameters);

        file.delete();

        return "upload finished";

    }



}
