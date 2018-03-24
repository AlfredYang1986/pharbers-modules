package com.pharbers.panel.format.input.writable.nhwa;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import com.pharbers.panel.format.input.writable.PhExcelWritable;

public class PhExcelCpaWritable extends PhExcelWritable {
    private final String delimiter = String.valueOf((char)31);

    private static Map<String, String> titleMap = new HashMap<String, String>() {{
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

    private String[] splitValues(String value) {
        return value.split(delimiter);
    }

    private String getCellKey(String[] lst, String flag) {
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
            return lst[4];
        }else if (flag.equals("HOSPITAL_CODE")) {
            return lst[5];
        }

        return "not implements";
        // throw new Exception("not implements");
    }

    private String getMin1InRow(String value) {
        String[] lst = splitValues(value);
        return getCellKey(lst, "PRODUCT_NAME") +
               getCellKey(lst, "APP2_COD") +
               getCellKey(lst, "PACK_DES") +
               getCellKey(lst, "PACK_NUMBER") +
               getCellKey(lst, "CORP_NAME");
    }

    private String getYearMonth(String value) {
        String[] lst = splitValues(value);
        return getCellKey(lst, "YEAR") +
               getCellKey(lst, "MONTH");
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
        return value + delimiter + "min1" + delimiter + "YM";
    }

    private String expendValues(String value) {
        return value + delimiter + getMin1InRow(value) + delimiter + getYearMonth(value);
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
