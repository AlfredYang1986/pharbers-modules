package com.pharbers.panel.format.input.writable.astellas;

import com.pharbers.panel.format.input.writable.PhExcelWritable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class phAstellasMarketsMatchWritable extends PhExcelWritable {
    private final String delimiter = String.valueOf((char)31);

    private static Map<String, String> titleMap = new HashMap<String, String>() {{
        put("药品名称", "MOLE_NAME");
        put("竞品市场", "MARKET");
    }};

    private String[] splitValues(String value) {
        return value.split(delimiter);
    }

    private String getCellKey(String[] lst, String flag) {
        if (flag.equals("MOLE_NAME")) {
            return lst[0];
        } else if (flag.equals("MARKET")) {
            return lst[1];
        }

        return "not implements";
    }

    private String transTitle2Eng(String value) {
        String[] lst = this.splitValues(value);
        List<String> result = new ArrayList<String>(lst.length);
        String reVal = "";
        for (String iter : lst) {
            result.add(titleMap.get(iter));
        }
        for (String iter : result) {
            reVal += iter + delimiter;
        }
        return reVal;
    }

    private String expendTitle(String value) {
        return value;
    }

    private String expendValues(String value) {
        int l = titleMap.size() - value.split(delimiter).length;
        String added = "";
        for(int i = 0; i < l ; i++){
            added = added + delimiter;
        }
        return value + added ;
    }

    @Override
    public String richWithInputRow(int index, String value) {
        if (index == 1) {
            return expendTitle(transTitle2Eng(value));
        } else return expendValues(value);
    }

    @Override
    public String getRowKey(String flag) {
        return getCellKey(splitValues(getValues()), flag);
    }
}
