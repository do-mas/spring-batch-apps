package com.batch.xlsx.step.mapper;

import org.apache.poi.ss.usermodel.Row;

public interface ExcelRowMapper<T> {
    T mapRow(Row rs) throws Exception;
}
