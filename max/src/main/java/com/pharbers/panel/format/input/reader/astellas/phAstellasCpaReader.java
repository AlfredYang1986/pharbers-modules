package com.pharbers.panel.format.input.reader.astellas;

import org.apache.hadoop.io.NullWritable;
import com.pharbers.panel.format.input.writable.PhExcelWritable;
import com.pharbers.panel.format.input.reader.common.PhExcelXLSXCommonReader;
import com.pharbers.panel.format.input.writable.astellas.phAstellasCpaWritable;

import java.io.IOException;

public class phAstellasCpaReader extends PhExcelXLSXCommonReader<NullWritable, PhExcelWritable> {

    @Override
    public NullWritable getCurrentKey() throws IOException, InterruptedException {
        return NullWritable.get();
    }

    @Override
    public PhExcelWritable getCurrentValue() throws IOException, InterruptedException {
        phAstellasCpaWritable reVal = new phAstellasCpaWritable();
        String s = reVal.richWithInputRow(parser.currentIndex(), value);
        reVal.setValues(s);
        return reVal;
    }
}