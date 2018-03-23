package com.pharbers.panel.format.input.writable.common;

import com.pharbers.panel.format.input.writable.PhExcelWritable;
import com.pharbers.panel.format.input.writable.writableStrategy.PhPanelStrategy;

public class PhExcelCommonWritable extends PhExcelWritable implements PhPanelStrategy {

    @Override
    public String richWithInputRow(int index, String value) {

        /**
         * @老齐，这里把中文变英文，配置文件最佳
         * 同样这里添加你需要添加的项，完善一行
         * 这里只能做一行内的操作，不能跨行
         */
        return value;
    }


}
