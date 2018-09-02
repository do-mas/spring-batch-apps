package com.batch.xlsx.step;

import org.springframework.batch.item.ItemProcessor;

public class RecordProcessor implements ItemProcessor<String, String> {

    @Override
    public String process(String val) {
        System.out.println("Processor, processing val: " + val);
        return val;
    }

}
