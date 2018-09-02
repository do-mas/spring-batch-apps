package com.batch.xlsx;

import org.springframework.batch.item.ItemProcessor;

public class RecordProcessor implements ItemProcessor<String, String> {

    @Override
    public String process(String o) throws Exception {

        try {
//            System.out.println("Processing " + o);
        } catch (Exception e) {
//            System.out.println("Processi");
            int i = 0;
        }
        return o;
    }

}
