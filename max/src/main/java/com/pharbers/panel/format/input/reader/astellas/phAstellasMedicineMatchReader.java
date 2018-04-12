package com.pharbers.panel.format.input.reader.astellas;

import com.pharbers.panel.format.input.reader.common.PhExcelXLSXCommonReader;
import com.pharbers.panel.format.input.writable.PhExcelWritable;
import com.pharbers.panel.format.input.writable.astellas.phAstellasMedicineMatchWritable;
import org.apache.hadoop.io.NullWritable;

import java.io.IOException;

public class phAstellasMedicineMatchReader extends PhExcelXLSXCommonReader<NullWritable, PhExcelWritable> {

    @Override
    public NullWritable getCurrentKey() throws IOException, InterruptedException {
        return NullWritable.get();
    }

    @Override
    public PhExcelWritable getCurrentValue() throws IOException, InterruptedException {
        phAstellasMedicineMatchWritable reVal = new phAstellasMedicineMatchWritable();
        String s = reVal.richWithInputRow(parser.currentIndex(), value);
        reVal.setValues(s);
        return reVal;
    }
}