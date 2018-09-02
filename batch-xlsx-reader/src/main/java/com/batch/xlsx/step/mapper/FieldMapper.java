package com.batch.xlsx.step.mapper;

import org.apache.poi.ss.usermodel.Row;

public class FieldMapper implements ExcelRowMapper<String> {

    public String mapRow(Row rs) {

        try {
            System.out.println("Reader mapping " + rs.getCell(0) + " " + rs.getCell(1));
        } catch (Exception e) {
            System.out.println("Reader: ERROR");
            return null;
        }
        return rs.getCell(0).toString();
    }

}
