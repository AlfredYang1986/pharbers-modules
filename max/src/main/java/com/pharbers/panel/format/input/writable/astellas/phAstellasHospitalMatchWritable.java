package com.pharbers.panel.format.input.writable.astellas;

import java.util.HashMap;

/**
 * Created by jeorch on 18-3-29.
 */
public class phAstellasHospitalMatchWritable extends phAstellasCommonWritable {

    public phAstellasHospitalMatchWritable() {
        titleMap = new HashMap<String, String>() {{
            put("PHA ID", "PHA_ID");
            put("标准_省", "Province");
            put("标准_市", "City");
        }};
    }

    @Override
    protected String getCellKey(String[] lst, String flag) {
        if (flag.equals("PHA_ID")) {
            return lst[0];
        } else if(flag.equals("Province")) {
            return lst[1];
        } else if (flag.equals("City")) {
            return lst[2];
        }

        return "not implements";
    }
}

