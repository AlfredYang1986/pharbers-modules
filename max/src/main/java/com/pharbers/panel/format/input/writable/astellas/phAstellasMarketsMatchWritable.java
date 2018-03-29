package com.pharbers.panel.format.input.writable.astellas;

import java.util.HashMap;
import com.pharbers.panel.format.input.writable.common.PhXlsxCommonWritable;

public class phAstellasMarketsMatchWritable extends PhXlsxCommonWritable {

    static {
        titleMap = new HashMap<String, String>() {{
            put("药品名称", "MOLE_NAME");
            put("竞品市场", "MARKET");
        }};
    }

    @Override
    protected String getCellKey(String[] lst, String flag) {
        if (flag.equals("MOLE_NAME")) {
            return lst[0];
        } else if (flag.equals("MARKET")) {
            return lst[1];
        }

        return "not implements";
    }

}
