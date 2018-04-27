package com.pharbers.panel.format.input.writable.pfizer;

public class phPfizerCpaWritable extends phPfizerCommonWritable {

    @Override
    protected String getCellKey(String[] lst, String flag) {
        if (flag.equals("CITY")) {
            return lst[0];
        } else if (flag.equals("YEAR")) {
            return lst[1];
        } else if (flag.equals("MONTH")) {
            if(lst[2].length() == 1) return "0" + lst[2];
            return lst[2];
        } else if (flag.equals("HOSPITAL_CODE")) {
            return lst[3];
        } else if (flag.equals("MOLE_NAME")) {
            return lst[4];
        } else if (flag.equals("PRODUCT_NAME")) {
            return lst[5].trim();
        } else if (flag.equals("PACK_DES")) {
            return lst[6].trim();
        }else if (flag.equals("PACK_NUMBER")) {
            return lst[7].trim();
        }else if (flag.equals("VALUE")) {
            return lst[8];
        }else if (flag.equals("STANDARD_UNIT")) {
            return lst[9];
        }else if (flag.equals("APP2_COD")) {
            return lst[10].trim();
        }else if (flag.equals("APP1_COD")) {
            return lst[11];
        }else if (flag.equals("CORP_NAME")) {
            return lst[12].trim();
        }else if (flag.equals("YM")) {
            return lst[13];
        }else if (flag.equals("min1")) {
            return lst[14];
        }

        return "not implements";
        // throw new Exception("not implements");
    }

    @Override
    protected String[] setCellKey(String[] lst, String flag, String value) {
        if (flag.equals("PRODUCT_NAME")) {
            lst[5] = value;
            return lst;
        } else if (flag.equals("PACK_DES")) {
            lst[6] = value;
            return lst;
        } else if (flag.equals("VALUE")) {
            lst[8] = value;
            return lst;
        } else if (flag.equals("STANDARD_UNIT")) {
            lst[9] = value;
            return lst;
        } else{
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

        lst = setCellKey(lst, "PACK_DES", getCellKey(lst, "PACK_DES"));

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
            return expendTitle(value);
        } else
            return prePanelFunction(value);
    }

}
