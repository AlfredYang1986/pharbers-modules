package com.pharbers.panel.format.input.reader.nhwa;

import java.io.IOException;
import org.apache.hadoop.io.NullWritable;
import com.pharbers.excel.format.input.writable.phExcelWritable;
import com.pharbers.panel.format.input.writable.nhwa.phNhwaCpaWritable;
import com.pharbers.excel.format.input.reader.common.PhExcelXLSXCommonReader;

public class phNhwaCpaReader extends PhExcelXLSXCommonReader<NullWritable, phExcelWritable> {

    @Override
    public NullWritable getCurrentKey() throws IOException, InterruptedException {
        return NullWritable.get();
    }

    @Override
    public phExcelWritable getCurrentValue() throws IOException, InterruptedException {
        phNhwaCpaWritable reVal = new phNhwaCpaWritable();
        String s = reVal.richWithInputRow(parser.currentIndex(), value);
        reVal.setValues(s);
        return reVal;
    }
}