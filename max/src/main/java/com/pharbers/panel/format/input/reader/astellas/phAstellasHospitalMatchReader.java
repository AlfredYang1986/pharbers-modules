package com.pharbers.panel.format.input.reader.astellas;

import java.io.IOException;
import org.apache.hadoop.io.NullWritable;
import com.pharbers.excel.format.input.writable.phExcelWritable;
import com.pharbers.excel.format.input.reader.common.PhExcelXLSXCommonReader;
import com.pharbers.panel.format.input.writable.astellas.phAstellasHospitalMatchWritable;

/**
 * Created by jeorch on 18-3-29.
 */
public class phAstellasHospitalMatchReader extends PhExcelXLSXCommonReader<NullWritable, phExcelWritable> {

    @Override
    public NullWritable getCurrentKey() throws IOException, InterruptedException {
        return NullWritable.get();
    }

    @Override
    public phExcelWritable getCurrentValue() throws IOException, InterruptedException {
        phAstellasHospitalMatchWritable reVal = new phAstellasHospitalMatchWritable();
        String s = reVal.richWithInputRow(parser.currentIndex(), value);
        reVal.setValues(s);
        return reVal;
    }
}