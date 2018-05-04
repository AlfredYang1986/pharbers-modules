package com.pharbers.panel.format.input.reader.common;

import java.io.IOException;
import com.pharbers.panel.format.input.writable.PhExcelWritable;
import com.pharbers.panel.format.input.writable.common.PhXlsCommonWritable;
import org.apache.hadoop.io.NullWritable;

public class PhExcelXLSReader extends PhExcelXLSCommonReader<NullWritable, PhExcelWritable> {
    @Override
    public NullWritable getCurrentKey() throws IOException, InterruptedException {
//        return new LongWritable(parser.currentIndex());
        return NullWritable.get();
    }

    @Override
    public PhExcelWritable getCurrentValue() throws IOException, InterruptedException {
        PhExcelWritable reVal = new PhXlsCommonWritable();
        reVal.setValues(value);
        return reVal;
    }
}
