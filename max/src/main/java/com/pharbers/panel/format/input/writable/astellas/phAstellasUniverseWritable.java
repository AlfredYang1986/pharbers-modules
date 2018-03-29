package com.pharbers.panel.format.input.writable.astellas;

import java.util.HashMap;
import com.pharbers.panel.format.input.writable.common.PhXlsxCommonWritable;

public class phAstellasUniverseWritable extends PhXlsxCommonWritable {

    static {
        titleMap = new HashMap<String, String>() {{
            put("公司", "COMPANY");
            put("年", "YEAR");
            put("市场", "MARKET");
            put("Segment", "SEGMENT");
            put("Factor", "FACTOR");
            put("If Panel_All", "IF_PANEL_ALL");
            put("If Panel_To Use", "IF_PANEL_TO_USE");
            put("样本医院编码", "HOSPITAL_CODE");
            put("PHA医院名称", "PHA_NAME");
            put("PHA ID", "PHA_ID");
            put("If County", "IF_COUNTY");
            put("Hosp_level", "HOSP_LEVEL");
            put("Region", "REGION");
        }};
    }

    @Override
    protected String getCellKey(String[] lst, String flag) {
        if (flag.equals("COMPANY")) {
            return lst[0];
        } else if (flag.equals("YEAR")) {
            return lst[1];
        } else if (flag.equals("MARKET")) {
            return lst[2];
        } else if (flag.equals("SEGMENT")) {
            return lst[3];
        } else if (flag.equals("FACTOR")) {
            return lst[4];
        } else if (flag.equals("IF_PANEL_ALL")) {
            return lst[5];
        } else if (flag.equals("IF_PANEL_TO_USE")) {
            return lst[6];
        } else if (flag.equals("HOSPITAL_CODE")) {
            return lst[7];
        } else if (flag.equals("PHA_NAME")) {
            return lst[8];
        } else if (flag.equals("PHA_ID")) {
            return lst[9];
        } else if (flag.equals("IF_COUNTY")) {
            return lst[10];
        } else if (flag.equals("HOSP_LEVEL")) {
            return lst[11];
        } else if (flag.equals("REGION")) {
            return lst[12];
        }

        return "not implements";
    }

    @Override
    protected String expendTitle(String value) {
        return value;
    }

    @Override
    protected String prePanelFunction(String value) {
        String[] lst = splitValues(value);

        return mkString(lst, delimiter);
    }

}
