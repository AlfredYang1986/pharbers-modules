package com.pharbers.panel.format.input.writable.pfizer;

public class phPfizerDeliveryUniverseWritable extends phPfizerCommonWritable {

    @Override
    protected String getCellKey(String[] lst, String flag) {
        if (flag.equals("PHA_ID")) {
            return lst[0];
        } else if (flag.equals("PROVINCE")) {
            return lst[1];
        } else if (flag.equals("CITY")) {
            return lst[2];
        } else if (flag.equals("CITY_TIER")) {
            return lst[3];
        } else if (flag.equals("BED_NUM")) {
            return lst[4];
        }

        return "not implements";
    }

    @Override
    protected String[] setCellKey(String[] lst, String flag, String value) {
        if (flag.equals("CITY")) {
            lst[2] = value;
            return lst;
        } else{
            return lst;
        }
    }

    @Override
    protected String prePanelFunction(String value) {
        String[] lst = splitValues(value);

        lst = setCellKey(lst, "CITY", getCellKey(lst, "CITY").replaceAll("市", ""));

        return mkString(lst, delimiter);
    }

    @Override
    public String richWithInputRow(int index, String value) {
        if (index == 1) {
            return expendTitle(value);
        } else
            return prePanelFunction(value);
    }

}
