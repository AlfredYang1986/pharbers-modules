package com.pharbers.panel.format.input.reader.pfizer;

import java.io.IOException;
import org.apache.hadoop.io.NullWritable;
import com.pharbers.excel.format.input.writable.phExcelWritable;
import com.pharbers.panel.format.input.writable.pfizer.phPfizerCpaWritable;
import com.pharbers.excel.format.input.reader.common.PhExcelXLSXCommonReader;

/**
 * Created by jeorch on 18-4-23.
 */
public class phPfizerCpaReader extends PhExcelXLSXCommonReader<NullWritable, phExcelWritable> {

    @Override
    public NullWritable getCurrentKey() throws IOException, InterruptedException {
        return NullWritable.get();
    }

    @Override
    public phExcelWritable getCurrentValue() throws IOException, InterruptedException {
        phPfizerCpaWritable reVal = new phPfizerCpaWritable();
        String s = reVal.richWithInputRow(parser.currentIndex(), value);
        reVal.setValues(s);
        return reVal;
    }
}