package com.batch;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {

    public static File convertToFile(MultipartFile multiPartFile) throws IOException {

        File convFile = new File(multiPartFile.getOriginalFilename());
        //TODO is that needed
        convFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(multiPartFile.getBytes());
        fos.close();

        return convFile;

    }
}
