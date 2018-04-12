package com.pharbers.panel.format.input.writable.common;

import com.pharbers.panel.format.input.writable.PhExcelWritable;

public class PhXlsxCommonWritable extends PhExcelWritable {

    @Override
    public String richWithInputRow(int index, String value) {
        return value;
    }

    @Override
    public String getRowKey(String flag) {
        return this.getValues();
    }
}
