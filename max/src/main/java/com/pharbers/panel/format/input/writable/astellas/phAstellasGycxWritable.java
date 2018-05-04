package com.pharbers.panel.format.input.writable.astellas;

import java.util.HashMap;

public class phAstellasGycxWritable extends phAstellasCommonWritable {

    public phAstellasGycxWritable() {
        titleMap = new HashMap<String, String>() {{
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
            put("金额(元)", "VALUE");
            put("数量(支/片)", "STANDARD_UNIT");
            put("剂型", "APP2_COD");
            put("途径", "APP1_COD");
            put("企业名称", "CORP_NAME");
        }};
    }

    @Override
    protected String getCellKey(String[] lst, String flag) {
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

    @Override
    protected String[] setCellKey(String[] lst, String flag, String value) {
        if (flag.equals("PROVINCES")) {
            lst[0] = value;
            return lst;
        } else if (flag.equals("CITY")) {
            lst[1] = value;
            return lst;
        } else if (flag.equals("YM")) {
            lst[2] = value;
            return lst;
        } else if (flag.equals("HOSPITAL_CODE")) {
            lst[3] = value;
            return lst;
        } else if (flag.equals("HOSPITAL_NAME")) {
            lst[4] = value;
            return lst;
        } else if (flag.equals("ATC_CODE")) {
            lst[5] = value;
            return lst;
        } else if (flag.equals("MOLE_NAME")) {
            lst[6] = value;
            return lst;
        }else if (flag.equals("PRODUCT_NAME")) {
            lst[7] = value;
            return lst;
        }else if (flag.equals("PACKAGE")) {
            lst[8] = value;
            return lst;
        }else if (flag.equals("PACK_NUMBER")) {
            lst[9] = value;
            return lst;
        }else if (flag.equals("PACK_DES")) {
            lst[10] = value;
            return lst;
        }else if (flag.equals("VALUE")) {
            lst[11] = value;
            return lst;
        }else if (flag.equals("STANDARD_UNIT")) {
            lst[12] = value;
            return lst;
        }else if (flag.equals("APP2_COD")) {
            lst[13] = value;
            return lst;
        }else if (flag.equals("APP1_COD")) {
            lst[14] = value;
            return lst;
        }else if (flag.equals("CORP_NAME")) {
            lst[15] = value;
            return lst;
        }else if (flag.equals("min1")) {
            lst[16] = value;
            return lst;
        }else{
            return lst;
        }
    }

    @Override
    protected String expendTitle(String value) {
        return value + delimiter + "min1";
    }

    @Override
    protected String prePanelFunction(String value) {
        String[] lst = splitValues(value);

        if ("新疆维吾尔自治区".equals(getCellKey(lst, "PROVINCES")))
            lst = setCellKey(lst, "PROVINCES", "新疆维吾尔族自治区");

        if ("".equals(getCellKey(lst, "PRODUCT_NAME")))
            lst = setCellKey(lst, "PRODUCT_NAME", getCellKey(lst, "MOLE_NAME"));

        if (Double.parseDouble(getCellKey(lst, "STANDARD_UNIT")) == 0)
            lst = setCellKey(lst, "VALUE", "0");

        if (Double.parseDouble(getCellKey(lst, "VALUE")) == 0)
            lst = setCellKey(lst, "STANDARD_UNIT", "0");

        String min1 = getCellKey(lst, "PRODUCT_NAME") +
                getCellKey(lst, "APP2_COD") +
                getCellKey(lst, "PACK_DES") +
                getCellKey(lst, "PACK_NUMBER") +
                getCellKey(lst, "CORP_NAME");

        return mkString(lst, delimiter) + delimiter + min1;
    }

    @Override
    public String richWithInputRow(int index, String value) {
        if (index == 1) {
            return expendTitle(transTitle2Eng(value));
        } else
            return expendValues(titleMap.size(), value);
    }

}
