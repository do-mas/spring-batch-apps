package com.batch.xlsx.step;


import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class RecordWriter implements ItemWriter<String> {

    @Override
    public void write(List<? extends String> list) {
        System.out.println("Writer, total records: " + list.size());
    }
}
