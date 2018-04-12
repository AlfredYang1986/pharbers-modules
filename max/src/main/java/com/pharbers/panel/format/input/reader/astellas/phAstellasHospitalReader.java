package com.pharbers.panel.format.input.reader.astellas;

import org.apache.hadoop.io.NullWritable;
import com.pharbers.panel.format.input.writable.PhExcelWritable;
import com.pharbers.panel.format.input.reader.common.PhExcelXLSXCommonReader;
import com.pharbers.panel.format.input.writable.astellas.phAstellasHospitalWritable;

import java.io.IOException;

public class phAstellasHospitalReader extends PhExcelXLSXCommonReader<NullWritable, PhExcelWritable> {

    @Override
    public NullWritable getCurrentKey() throws IOException, InterruptedException {
        return NullWritable.get();
    }

    @Override
    public PhExcelWritable getCurrentValue() throws IOException, InterruptedException {
        phAstellasHospitalWritable reVal = new phAstellasHospitalWritable();
        String s = reVal.richWithInputRow(parser.currentIndex(), value);
        reVal.setValues(s);
        return reVal;
    }
}