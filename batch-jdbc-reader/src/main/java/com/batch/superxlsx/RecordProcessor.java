package com.batch.superxlsx;

import org.springframework.batch.item.ItemProcessor;

public class RecordProcessor implements ItemProcessor<String, String> {

    @Override
    public String process(String o) throws Exception {

        try {
            System.out.println("Processing " + o);
        } catch (Exception e) {
            System.out.println("ERROR");
            int i = 0;
        }
        return o;
    }

}
