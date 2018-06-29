package com.pharbers.panel.format.input.reader.pfizer;

import com.pharbers.excel.format.input.reader.common.PhExcelXLSXCommonReader;
import com.pharbers.excel.format.input.writable.phExcelWritable;
import com.pharbers.panel.format.input.writable.pfizer.phPfizerDeliveryUniverseWritable;
import org.apache.hadoop.io.NullWritable;

import java.io.IOException;

public class phPfizerDeliveryUniverseReader extends PhExcelXLSXCommonReader<NullWritable, phExcelWritable> {

    @Override
    public NullWritable getCurrentKey() throws IOException, InterruptedException {
        return NullWritable.get();
    }

    @Override
    public phExcelWritable getCurrentValue() throws IOException, InterruptedException {
        phPfizerDeliveryUniverseWritable reVal = new phPfizerDeliveryUniverseWritable();
        String s = reVal.richWithInputRow(parser.currentIndex(), value);
        reVal.setValues(s);
        return reVal;
    }
}