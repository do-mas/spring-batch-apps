package com.batch.xlsx;

import exc.orig.RowMapper;
import exc.orig.ExcelRowSet;

public class ExcelFieldMapper implements RowMapper<String> {

    @Override
    public String mapRow(ExcelRowSet rs) {
        try {
            System.out.println("Reader mapping " + rs.getColumnValue(0) + " " + rs.getColumnValue(1));
        } catch (Exception e) {
            System.out.println("Reader EEEEEEEEEEEEEEERRRR");
            return null;
        }
        return rs.getColumnValue(0).toString();
    }

}
