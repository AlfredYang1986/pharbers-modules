package com.pharbers.panel.format.input.reader.nhwa;

import com.pharbers.panel.format.input.reader.common.PhExcelXLSXCommonReader;
import com.pharbers.panel.format.input.writable.PhExcelWritable;
import com.pharbers.panel.format.input.writable.nhwa.phNhwaCpaSecondSheetWritable;
import com.pharbers.panel.format.input.writable.nhwa.phNhwaCpaWritable;
import org.apache.hadoop.io.NullWritable;

import java.io.IOException;

public class phNhwaCpaSecondSheetReader extends PhExcelXLSXCommonReader<NullWritable, PhExcelWritable> {

    @Override
    public NullWritable getCurrentKey() throws IOException, InterruptedException {
        return NullWritable.get();
    }

    @Override
    public PhExcelWritable getCurrentValue() throws IOException, InterruptedException {
        phNhwaCpaSecondSheetWritable reVal = new phNhwaCpaSecondSheetWritable();
        String s = reVal.richWithInputRow(parser.currentIndex(), value);
        reVal.setValues(s);
        return reVal;
    }
}