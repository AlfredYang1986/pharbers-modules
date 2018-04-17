package com.pharbers.panel.format.input.writable.astellas;

import java.util.HashMap;

public class phAstellasUniverseWritable extends phAstellasCommonWritable {

    public phAstellasUniverseWritable() {
        titleMap = new HashMap<String, String>() {{
            put("公司", "COMPANY");
            put("年", "YEAR");
            put("市场", "MARKET");
            put("Segment", "SEGMENT");
            put("Factor", "FACTOR");
            put("If Panel_All", "IF_PANEL_ALL");
            put("If Panel_To Use", "IF_PANEL_TO_USE");
            put("样本医院编码", "PANEL_ID");
            put("PHA医院名称", "PHA_NAME");
            put("PHA ID", "PHA_ID");
            put("If County", "IF_COUNTY");
            put("Hosp_level", "HOSP_LEVEL");
            put("Region", "REGION");
        }};
    }

    @Override
    protected String getCellKey(String[] lst, String flag) {
        if (flag.equals("PANEL_ID")) {
            return lst[7];
        } else if (flag.equals("PHA_ID")) {
            return lst[9];
        }

        return "not implements";
    }

}
