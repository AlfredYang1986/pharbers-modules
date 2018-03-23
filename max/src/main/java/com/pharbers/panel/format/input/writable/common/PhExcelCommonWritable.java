package com.pharbers.panel.format.input.writable.common;

import com.pharbers.panel.format.input.writable.PhExcelWritable;
import com.pharbers.panel.format.input.writable.writableStrategy.PhPanelStrategy;

public class PhExcelCommonWritable extends PhExcelWritable {

    @Override
    public String richWithInputRow(int index, String value) {
        return value;
    }

    @Override
    public String getRowKey(String flag) {
        return this.getValues();
    }
}
