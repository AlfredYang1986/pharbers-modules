package com.pharbers.panel.format.input.reader.astellas;

import java.io.IOException;
import org.apache.hadoop.io.NullWritable;
import com.pharbers.panel.format.input.writable.PhExcelWritable;
import com.pharbers.panel.format.input.writable.astellas.PhXlsCpaWritable;
import com.pharbers.panel.format.input.reader.common.PhExcelXLSCommonReader;

public class PhXlsCpaReader extends PhExcelXLSCommonReader<NullWritable, PhExcelWritable> {

    @Override
    public NullWritable getCurrentKey() throws IOException, InterruptedException {
        return NullWritable.get();
    }

    @Override
    public PhExcelWritable getCurrentValue() throws IOException, InterruptedException {
        PhXlsCpaWritable reVal = new PhXlsCpaWritable();
        String s = reVal.richWithInputRow(parser.currentIndex(), value);
        reVal.setValues(s);
        return reVal;
    }
}