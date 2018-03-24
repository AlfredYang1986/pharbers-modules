package com.pharbers.panel.format.input.reader.common;

import java.io.IOException;
import org.apache.hadoop.io.LongWritable;
import com.pharbers.panel.format.input.writable.PhExcelWritable;
import com.pharbers.panel.format.input.writable.common.PhExcelCommonWritable;

public class PhExcelXLSXReader extends PhExcelXLSXCommonReader<LongWritable, PhExcelWritable> {
    @Override
    public LongWritable getCurrentKey() throws IOException, InterruptedException {
        return new LongWritable(parser.currentIndex());
    }

    @Override
    public PhExcelWritable getCurrentValue() throws IOException, InterruptedException {
        PhExcelWritable reVal = new PhExcelCommonWritable();
        reVal.setValues(value);
        return reVal;
    }
}
