package com.pharbers.panel.format.input.reader.nhwa;

import java.io.IOException;
import org.apache.hadoop.io.NullWritable;
import com.pharbers.panel.format.input.writable.PhExcelWritable;
import com.pharbers.panel.format.input.writable.nhwa.PhXlsxCpaWritable;
import com.pharbers.panel.format.input.reader.common.PhExcelXLSXCommonReader;

public class PhXlsxCpaReader extends PhExcelXLSXCommonReader<NullWritable, PhExcelWritable> {

    @Override
    public NullWritable getCurrentKey() throws IOException, InterruptedException {
        return NullWritable.get();
    }

    @Override
    public PhExcelWritable getCurrentValue() throws IOException, InterruptedException {
        PhXlsxCpaWritable reVal = new PhXlsxCpaWritable();
        String s = reVal.richWithInputRow(parser.currentIndex(), value);
        reVal.setValues(s);
        return reVal;
    }
}