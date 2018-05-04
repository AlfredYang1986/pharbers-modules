package com.pharbers.panel.format.input.reader.pfizer;

import com.pharbers.panel.format.input.reader.common.PhExcelXLSXCommonReader;
import com.pharbers.panel.format.input.writable.PhExcelWritable;
import com.pharbers.panel.format.input.writable.pfizer.phPfizerGycxWritable;
import org.apache.hadoop.io.NullWritable;

import java.io.IOException;

/**
 * Created by jeorch on 18-4-23.
 */
public class phPfizerGycxReader extends PhExcelXLSXCommonReader<NullWritable, PhExcelWritable> {

    @Override
    public NullWritable getCurrentKey() throws IOException, InterruptedException {
        return NullWritable.get();
    }

    @Override
    public PhExcelWritable getCurrentValue() throws IOException, InterruptedException {
        phPfizerGycxWritable reVal = new phPfizerGycxWritable();
        String s = reVal.richWithInputRow(parser.currentIndex(), value);
        reVal.setValues(s);
        return reVal;
    }
}