package com.pharbers.panel.format.input.writable.nhwa;

import java.util.Map;
import java.util.HashMap;
import com.pharbers.panel.format.input.writable.common.PhXlsxCommonWritable;

public class PhXlsxCpaWritable extends PhXlsxCommonWritable {

    private Map<String, String> titleMap = new HashMap<String, String>() {{
        put("省", "PROVINCES");
        put("城市", "CITY");
        put("年", "YEAR");
        put("季度", "QUARTER");
        put("月", "MONTH");
        put("医院编码", "HOSPITAL_CODE");
        put("ATC编码", "ATC_CODE");
        put("ATC码", "ATC_CODE");
        put("药品名称", "MOLE_NAME");
        put("商品名", "PRODUCT_NAME");
        put("包装", "PACKAGE");
        put("药品规格", "PACK_DES");
        put("规格", "PACK_DES");
        put("包装数量", "PACK_NUMBER");
        put("金额（元）", "VALUE");
        put("数量（支/片）", "STANDARD_UNIT");
        put("剂型", "APP2_COD");
        put("给药途径", "APP1_COD");
        put("途径", "APP1_COD");
        put("生产企业", "CORP_NAME");
    }};

    @Override
    protected String getCellKey(String[] lst, String flag) {
        if (flag.equals("YEAR")) {
            return lst[2];
        } else if (flag.equals("PRODUCT_NAME")) {
            return lst[8];
        }else if (flag.equals("APP2_COD")) {
            return lst[14];
        }else if (flag.equals("PACK_DES")) {
            return lst[10];
        }else if (flag.equals("PACK_NUMBER")) {
            return lst[11];
        }else if (flag.equals("CORP_NAME")) {
            return lst[16];
        }else if (flag.equals("MONTH")) {
            if(lst[4].length() == 1) return "0" + lst[4];
            else return lst[4];
        }else if (flag.equals("HOSPITAL_CODE")) {
            return lst[5];
        }else if (flag.equals("YM")) {
            return lst[18];
        }

        return "not implements";
        // throw new Exception("not implements");
    }

    @Override
    protected String expendTitle(String value) {
        return value + "min1" + delimiter + "YM";
    }

    @Override
    protected String prePanelFunction(String value) {
        String[] lst = splitValues(value);
        String ym = getCellKey(lst, "YEAR") +
                getCellKey(lst, "MONTH");
        String min1 = getCellKey(lst, "PRODUCT_NAME") +
                getCellKey(lst, "APP2_COD") +
                getCellKey(lst, "PACK_DES") +
                getCellKey(lst, "PACK_NUMBER") +
                getCellKey(lst, "CORP_NAME");



        return mkString(lst, delimiter) + delimiter + min1 + delimiter + ym;
    }

}
