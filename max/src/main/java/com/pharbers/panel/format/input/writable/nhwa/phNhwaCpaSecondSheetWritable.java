package com.pharbers.panel.format.input.writable.nhwa;

import java.util.HashMap;

public class phNhwaCpaSecondSheetWritable extends phNhwaCommonWritable {

    public phNhwaCpaSecondSheetWritable() {
        super.titleMap = new HashMap<String, String>() {{
            put("医院编码", "医院编码");
            put("月份", "月份");
            put("", "na");
        }};
    }

    @Override
    protected String expendTitle(String value) {
        return value + delimiter + "na";
    }

    @Override
    protected String prePanelFunction(String value) {
        return fullTail(value, 3);
    }

    private String fullTail(String value, int tl) {

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

}
