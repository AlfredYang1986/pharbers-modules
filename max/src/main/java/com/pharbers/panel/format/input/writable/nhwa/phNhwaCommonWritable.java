package com.pharbers.panel.format.input.writable.nhwa;

import com.pharbers.excel.format.input.writable.phExcelWritable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class phNhwaCommonWritable extends phExcelWritable {
    public Map<String, String> titleMap = null;

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

    protected String mkString(String[] lst, String seq) {
        StringBuilder temp = new StringBuilder();
        for (String i : lst) {
            temp.append(i).append(seq);
        }
        return temp.deleteCharAt(temp.length() - 1).toString();
    }

    String[] splitValues(String value) {
        return value.split(delimiter);
    }

    protected String transTitle2Eng(String value) {
        String[] lst = this.splitValues(value);
        List<String> result = new ArrayList<>(lst.length);
        StringBuilder reVal = new StringBuilder();
        for (String iter : lst) {
            String tmp = titleMap.get(iter);
            result.add(tmp == null ? iter : tmp);
        }
        for (String iter : result) {
            reVal.append(iter).append(delimiter);
        }
        return reVal.deleteCharAt(reVal.length() - 1).toString();
    }

    protected String expendTitle(String value) {
        return value;
    }

    private String expendValues(String value) {
        return prePanelFunction(value);
    }

    protected String getCellKey(String[] lst, String flag) {
        return "not implements";
    }

    protected String[] setCellKey(String[] lst, String flag, String value) {
        return lst;
    }

    protected String prePanelFunction(String value) {
        String[] lst = splitValues(value);
        return mkString(lst, delimiter);
    }
}
