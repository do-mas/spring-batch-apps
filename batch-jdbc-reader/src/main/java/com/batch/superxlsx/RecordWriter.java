package com.batch.superxlsx;


import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.transform.FieldSet;

import java.util.List;

public class RecordWriter implements ItemWriter<String> {

    @Override
    public void write(List<? extends String> list) throws Exception {
        System.out.println("writing total records: " + list.size());
    }
}
