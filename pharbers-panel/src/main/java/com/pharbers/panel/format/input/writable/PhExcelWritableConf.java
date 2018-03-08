package com.pharbers.panel.format.input.writable;

import java.util.ArrayList;

public class PhExcelWritableConf {

    private static PhExcelWritableConf ourInstance = new PhExcelWritableConf();

    public static PhExcelWritableConf getInstance() {
        return ourInstance;
    }

    private PhExcelWritableConf() {
    }

    private ArrayList<String> lst = new ArrayList<String>(20);

    public Integer getWritableColCount() {
        if (lst.isEmpty()) return 17;
        else return lst.size();
    }
}
