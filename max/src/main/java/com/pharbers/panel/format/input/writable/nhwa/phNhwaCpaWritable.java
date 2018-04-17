package com.pharbers.panel.format.input.writable.nhwa;

import java.util.HashMap;

public class phNhwaCpaWritable extends phNhwaCommonWritable {

    public phNhwaCpaWritable() {
        super.titleMap = new HashMap<String, String>() {{
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
    }

    @Override
    protected String getCellKey(String[] lst, String flag) {
        if (flag.equals("PROVINCES")) {
            return lst[0];
        } else if (flag.equals("CITY")) {
            return lst[1];
        } else if (flag.equals("YEAR")) {
            return lst[2];
        } else if (flag.equals("QUARTER")) {
            return lst[3];
        } else if (flag.equals("MONTH")) {
            if(lst[4].length() == 1) return "0" + lst[4];
            else return lst[4];
        } else if (flag.equals("HOSPITAL_CODE")) {
            return lst[5];
        } else if (flag.equals("ATC_CODE")) {
            return lst[6];
        }else if (flag.equals("MOLE_NAME")) {
            return lst[7];
        }else if (flag.equals("PRODUCT_NAME")) {
            return lst[8];
        }else if (flag.equals("PACKAGE")) {
            return lst[9];
        }else if (flag.equals("PACK_DES")) {
            return lst[10];
        }else if (flag.equals("PACK_NUMBER")) {
            return lst[11];
        }else if (flag.equals("VALUE")) {
            return lst[12];
        }else if (flag.equals("STANDARD_UNIT")) {
            return lst[13];
        }else if (flag.equals("APP2_COD")) {
            return lst[14];
        }else if (flag.equals("APP1_COD")) {
            return lst[15];
        }else if (flag.equals("CORP_NAME")) {
            return lst[16];
        }else if (flag.equals("min1")) {
            return lst[17];
        }else if (flag.equals("YM")) {
            return lst[18];
        }

        return "not implements";
        // throw new Exception("not implements");
    }

    @Override
    protected String[] setCellKey(String[] lst, String flag, String value) {
        if (flag.equals("PRODUCT_NAME")) {
            lst[8] = value;
            return lst;
        }else if (flag.equals("VALUE")) {
            lst[12] = value;
            return lst;
        }else if (flag.equals("STANDARD_UNIT")) {
            lst[13] = value;
            return lst;
        }else{
            return lst;
        }
    }

    @Override
    protected String expendTitle(String value) {
        return value + delimiter + "min1" + delimiter + "YM";
    }

    @Override
    protected String prePanelFunction(String value) {
        String[] lst = splitValues(value);

        if("".equals(getCellKey(lst, "PRODUCT_NAME")))
            lst = setCellKey(lst, "PRODUCT_NAME", getCellKey(lst, "MOLE_NAME"));
        if("".equals(getCellKey(lst, "VALUE")))
            lst = setCellKey(lst, "VALUE", "0");
        if("".equals(getCellKey(lst, "STANDARD_UNIT")))
            lst = setCellKey(lst, "STANDARD_UNIT", "0");

        String min1 = getCellKey(lst, "PRODUCT_NAME") +
                getCellKey(lst, "APP2_COD") +
                getCellKey(lst, "PACK_DES") +
                getCellKey(lst, "PACK_NUMBER") +
                getCellKey(lst, "CORP_NAME");

        String ym = getCellKey(lst, "YEAR") + getCellKey(lst, "MONTH");

        return mkString(lst, delimiter) + delimiter + min1 + delimiter + ym ;
    }

}
