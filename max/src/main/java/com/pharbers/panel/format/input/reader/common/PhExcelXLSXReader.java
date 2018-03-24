package com.pharbers.panel.format.input.reader.common;

import java.io.IOException;
import com.pharbers.panel.format.input.writable.PhExcelWritable;
import com.pharbers.panel.format.input.writable.common.PhXlsxCommonWritable;
import com.pharbers.panel.format.input.writable.common.PhExcelCommonWritable;
import org.apache.hadoop.io.NullWritable;

public class PhExcelXLSXReader extends PhExcelXLSXCommonReader<NullWritable, PhExcelWritable> {
    @Override
    public NullWritable getCurrentKey() throws IOException, InterruptedException {
        return NullWritable.get();
    }

    @Override
    public PhExcelWritable getCurrentValue() throws IOException, InterruptedException {
        PhExcelWritable reVal = new PhXlsxCommonWritable();
        reVal.setValues(value);
        return reVal;
    }
}
