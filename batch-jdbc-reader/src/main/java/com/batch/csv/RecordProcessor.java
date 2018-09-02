package com.batch.csv;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.transform.FieldSet;

public class RecordProcessor implements ItemProcessor<FieldSet, FieldSet> {

    @Override
    public FieldSet process(FieldSet o) throws Exception {
        System.out.println("processing " + o.toString());
        return o;
    }

}
