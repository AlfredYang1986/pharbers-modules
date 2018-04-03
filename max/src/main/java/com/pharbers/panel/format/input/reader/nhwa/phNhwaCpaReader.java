package com.pharbers.panel.format.input.reader.nhwa;

import java.io.IOException;
import org.apache.hadoop.io.NullWritable;
import com.pharbers.panel.format.input.writable.PhExcelWritable;
import com.pharbers.panel.format.input.writable.nhwa.phNhwaCpaWritable;
import com.pharbers.panel.format.input.reader.common.PhExcelXLSXCommonReader;

public class phNhwaCpaReader extends PhExcelXLSXCommonReader<NullWritable, PhExcelWritable> {

    @Override
    public NullWritable getCurrentKey() throws IOException, InterruptedException {
        return NullWritable.get();
    }

    @Override
    public PhExcelWritable getCurrentValue() throws IOException, InterruptedException {
        phNhwaCpaWritable reVal = new phNhwaCpaWritable();
        String s = reVal.richWithInputRow(parser.currentIndex(), value);
        reVal.setValues(s);
        return reVal;
    }
}