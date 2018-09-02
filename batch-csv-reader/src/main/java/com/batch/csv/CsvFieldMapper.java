package com.batch.csv;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class CsvFieldMapper implements FieldSetMapper<FieldSet> {

    @Override
    public FieldSet mapFieldSet(FieldSet fieldSet) throws BindException {
        System.out.println("Reader: mapping fields.." +fieldSet.toString());
        return fieldSet;
    }
}
