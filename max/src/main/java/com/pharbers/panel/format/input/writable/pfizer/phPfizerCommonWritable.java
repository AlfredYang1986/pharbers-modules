package com.pharbers.panel.format.input.writable.pfizer;

import com.pharbers.excel.format.input.writable.phExcelWritable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class phPfizerCommonWritable extends phExcelWritable {
    protected static Map<String, String> titleMap = null;

    @Override
    public String richWithInputRow(int index, String value) {
        if (index == 1) {
            return expendTitle(transTitle2Eng(value));
        } else return expendValues(titleMap.size(), value);
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

    protected String fullTail(String value, int tl) {

        String[] rLst = value.split(delimiter);
        int rl = rLst.length;

        if(tl > rl){
            String[] resultLst = new String[tl];

            for (int i = 0; i < rl; i++) {
                resultLst[i] = rLst[i];
            }

            for (int i = 0; i < tl - rl; i++) {
                resultLst[rl + i] = " ";
            }
            return mkString(resultLst, delimiter);
        }else{
            return value;
        }
    }

    String expendValues(int tl, String value) {
        String fullString = fullTail(value, tl);
        return prePanelFunction(fullString);
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
