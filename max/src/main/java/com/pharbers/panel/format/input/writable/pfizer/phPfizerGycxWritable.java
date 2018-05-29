package com.pharbers.panel.format.input.writable.pfizer;

import java.util.HashMap;

public class phPfizerGycxWritable extends phPfizerCommonWritable {

    public phPfizerGycxWritable() {
        titleMap = new HashMap<String, String>() {{
            put("省/自治区/直辖市", "PROVINCE");
            put("城市", "CITY");
            put("年", "YEAR");
            put("月", "MONTH");
            put("医院编码", "HOSPITAL_CODE");
            put("通用名", "MOLE_NAME");
            put("药品商品名", "PRODUCT_NAME");
            put("规格", "PACK_DES");
            put("包装规格", "PACK_NUMBER");
            put("金额", "VALUE");
            put("最小制剂单位数量", "STANDARD_UNIT");
            put("剂型", "APP2_COD");
            put("给药途径", "APP1_COD");
            put("生产企业", "CORP_NAME");
        }};
    }

    @Override
    protected String getCellKey(String[] lst, String flag) {
        if (flag.equals("PROVINCE")) {
            return lst[0];
        } else if (flag.equals("CITY")) {
            return lst[1];
        } else if (flag.equals("YEAR")) {
            return lst[2];
        } else if (flag.equals("MONTH")) {
            if(lst[3].length() == 1) return "0" + lst[3];
            return lst[3];
        } else if (flag.equals("HOSPITAL_CODE")) {
            return lst[4];
        } else if (flag.equals("MOLE_NAME")) {
            return lst[5];
        } else if (flag.equals("PRODUCT_NAME")) {
            return lst[6];
        } else if (flag.equals("PACK_DES")) {
            return lst[7];
        }else if (flag.equals("PACK_NUMBER")) {
            return lst[8];
        }else if (flag.equals("VALUE")) {
            return lst[9];
        }else if (flag.equals("STANDARD_UNIT")) {
            return lst[10];
        }else if (flag.equals("APP2_COD")) {
            return lst[11];
        }else if (flag.equals("APP1_COD")) {
            return lst[12];
        }else if (flag.equals("CORP_NAME")) {
            return lst[13];
        }else if (flag.equals("YM")) {
            return lst[14];
        }else if (flag.equals("min1")) {
            return lst[15];
        }

        return "not implements";
        // throw new Exception("not implements");
    }

    @Override
    protected String[] setCellKey(String[] lst, String flag, String value) {
        if (flag.equals("PROVINCE")) {
            lst[0] = value;
            return lst;
        } else if (flag.equals("CITY")) {
            lst[1] = value;
            return lst;
        } else if (flag.equals("YEAR")) {
            lst[2] = value;
            return lst;
        } else if (flag.equals("MONTH")) {
            lst[3] = value;
            return lst;
        } else if (flag.equals("HOSPITAL_CODE")) {
            lst[4] = value;
            return lst;
        } else if (flag.equals("MOLE_NAME")) {
            lst[5] = value;
            return lst;
        } else if (flag.equals("PRODUCT_NAME")) {
            lst[6] = value;
            return lst;
        } else if (flag.equals("PACK_DES")) {
            lst[7] = value;
            return lst;
        }else if (flag.equals("PACK_NUMBER")) {
            lst[8] = value;
            return lst;
        }else if (flag.equals("VALUE")) {
            lst[9] = value;
            return lst;
        }else if (flag.equals("STANDARD_UNIT")) {
            lst[10] = value;
            return lst;
        }else if (flag.equals("APP2_COD")) {
            lst[11] = value;
            return lst;
        }else if (flag.equals("APP1_COD")) {
            lst[12] = value;
            return lst;
        }else if (flag.equals("CORP_NAME")) {
            lst[13] = value;
            return lst;
        }else if (flag.equals("YM")) {
            lst[14] = value;
            return lst;
        }else if (flag.equals("min1")) {
            lst[15] = value;
            return lst;
        }else{
            return lst;
        }
    }

    @Override
    protected String expendTitle(String value) {
        return value + delimiter + "YM" + delimiter + "min1";
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

        String ym = getCellKey(lst, "YEAR") + getCellKey(lst, "MONTH");

        String min1 = getCellKey(lst, "PRODUCT_NAME") +
                getCellKey(lst, "APP2_COD") +
                getCellKey(lst, "PACK_DES") +
                getCellKey(lst, "PACK_NUMBER") +
                getCellKey(lst, "CORP_NAME");

        return mkString(lst, delimiter) + delimiter + ym + delimiter + min1;
    }

    @Override
    public String richWithInputRow(int index, String value) {
        if (index == 1) {
            return expendTitle(transTitle2Eng(value));
        } else
            return expendValues(titleMap.size(), value);
    }

}
