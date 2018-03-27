package com.pharbers.panel.format.input.writable.astellas;


import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import com.pharbers.panel.format.input.writable.PhExcelWritable;

public class PhXlsxCpaWritable extends PhExcelWritable {
    private final String delimiter = String.valueOf((char)31);

    private static Map<String, String> titleMap = new HashMap<String, String>() {{
        put("省份", "PROVINCES");
        put("城市", "CITY");
        put("年月", "YM");
        put("医院", "HOSPITAL_CODE");
        put("医院名称", "HOSPITAL_NAME");
        put("ATC编码", "ATC_CODE");
        put("药品名称", "MOLE_NAME");
        put("商品名", "PRODUCT_NAME");
        put("包装", "PACKAGE");
        put("包装数量", "PACK_NUMBER");
        put("规格", "PACK_DES");
        put("金额（元）", "VALUE");
        put("数量(支/片)", "STANDARD_UNIT");
        put("剂型", "APP2_COD");
        put("途径", "APP1_COD");
        put("企业名称", "CORP_NAME");
    }};

    private String[] splitValues(String value) {
        return value.split(delimiter);
    }

    private String getCellKey(String[] lst, String flag) {
        if (flag.equals("PROVINCES")) {
            return lst[0];
        } else if (flag.equals("CITY")) {
            return lst[1];
        } else if (flag.equals("YM")) {
            return lst[2];
        } else if (flag.equals("HOSPITAL_CODE")) {
            return lst[3];
        } else if (flag.equals("HOSPITAL_NAME")) {
            return lst[4];
        } else if (flag.equals("ATC_CODE")) {
            return lst[5];
        } else if (flag.equals("MOLE_NAME")) {
            return lst[6];
        }else if (flag.equals("PRODUCT_NAME")) {
            return lst[7];
        }else if (flag.equals("PACKAGE")) {
            return lst[8];
        }else if (flag.equals("PACK_NUMBER")) {
            return lst[9];
        }else if (flag.equals("PACK_DES")) {
            return lst[10];
        }else if (flag.equals("VALUE")) {
            return lst[11];
        }else if (flag.equals("STANDARD_UNIT")) {
            return lst[12];
        }else if (flag.equals("APP2_COD")) {
            return lst[13];
        }else if (flag.equals("APP1_COD")) {
            return lst[14];
        }else if (flag.equals("CORP_NAME")) {
            return lst[15];
        }else if (flag.equals("min1")) {
            return lst[16];
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
        return value + "min1";
    }

    private String expendValues(String value) {
        return value + delimiter + getMin1InRow(value);
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
