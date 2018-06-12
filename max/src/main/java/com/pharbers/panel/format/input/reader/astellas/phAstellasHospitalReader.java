package com.pharbers.panel.format.input.reader.astellas;

import org.apache.hadoop.io.NullWritable;
import com.pharbers.excel.format.input.writable.phExcelWritable;
import com.pharbers.excel.format.input.reader.common.PhExcelXLSXCommonReader;
import com.pharbers.panel.format.input.writable.astellas.phAstellasHospitalWritable;

import java.io.IOException;

public class phAstellasHospitalReader extends PhExcelXLSXCommonReader<NullWritable, phExcelWritable> {

    @Override
    public NullWritable getCurrentKey() throws IOException, InterruptedException {
        return NullWritable.get();
    }

    @Override
    public phExcelWritable getCurrentValue() throws IOException, InterruptedException {
        phAstellasHospitalWritable reVal = new phAstellasHospitalWritable();
        String s = reVal.richWithInputRow(parser.currentIndex(), value);
        reVal.setValues(s);
        return reVal;
    }
}