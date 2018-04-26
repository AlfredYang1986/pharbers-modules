package com.pharbers.panel.format.input.writable.astellas;

import java.util.HashMap;

public class phAstellasMedicineMatchWritable extends phAstellasCommonWritable {

    public phAstellasMedicineMatchWritable() {
        titleMap = new HashMap<String, String>() {{
            put("药品名称1", "MOLE_NAME1");
            put("商品名1", "PRODUCT_NAME1");
            put("剂型1", "APP2_COD1");
            put("药品规格1", "PACK_DES1");
            put("包装数量1", "PACK_NUMBER1");
            put("企业名称1", "CORP_NAME1");
            put("min1", "min1");
            put("无分隔", "min0");
            put("标准药品名称", "STANDARD_MOLE_NAME");
            put("标准商品名", "STANDARD_PRODUCT_NAME");
            put("标准剂型", "STANDARD_APP2_COD");
            put("标准规格", "STANDARD_PACK_DES");
            put("包装数量2", "PACK_NUMBER2");
            put("标准企业", "STANDARD_CORP_NAME");
            put("min2", "min2");
            put("Pack_ID", "PACK_ID");
        }};
    }

    @Override
    protected String getCellKey(String[] lst, String flag) {
        if (flag.equals("MOLE_NAME1")) {
            return lst[0];
        } else if (flag.equals("PRODUCT_NAME1")) {
            return lst[1];
        } else if (flag.equals("APP2_COD1")) {
            return lst[2];
        } else if (flag.equals("PACK_DES1")) {
            return lst[3];
        } else if (flag.equals("PACK_NUMBER1")) {
            return lst[4];
        } else if (flag.equals("CORP_NAME1")) {
            return lst[5];
        } else if (flag.equals("min1")) {
            return lst[6];
        } else if (flag.equals("min0")) {
            return lst[7];
        } else if (flag.equals("STANDARD_MOLE_NAME")) {
            return lst[8];
        } else if (flag.equals("STANDARD_PRODUCT_NAME")) {
            return lst[9];
        } else if (flag.equals("STANDARD_APP2_COD")) {
            return lst[10];
        } else if (flag.equals("STANDARD_PACK_DES")) {
            return lst[11];
        } else if (flag.equals("PACK_NUMBER2")) {
            return lst[12];
        } else if (flag.equals("STANDARD_CORP_NAME")) {
            return lst[13];
        } else if (flag.equals("min2")) {
            return lst[14];
        } else if (flag.equals("PACK_ID")) {
            return lst[15];
        }

        return "not implements";
        // throw new Exception("not implements");
    }

    @Override
    protected String[] setCellKey(String[] lst, String flag, String value) {
        if (flag.equals("MOLE_NAME1")) {
            lst[0] = value;
            return lst;
        } else if (flag.equals("PRODUCT_NAME1")) {
            lst[1] = value;
            return lst;
        } else if (flag.equals("APP2_COD1")) {
            lst[2] = value;
            return lst;
        } else if (flag.equals("PACK_DES1")) {
            lst[3] = value;
            return lst;
        } else if (flag.equals("PACK_NUMBER1")) {
            lst[4] = value;
            return lst;
        } else if (flag.equals("CORP_NAME1")) {
            lst[5] = value;
            return lst;
        } else if (flag.equals("min1")) {
            lst[6] = value;
            return lst;
        } else if (flag.equals("min0")) {
            lst[7] = value;
            return lst;
        } else if (flag.equals("STANDARD_MOLE_NAME")) {
            lst[8] = value;
            return lst;
        } else if (flag.equals("STANDARD_PRODUCT_NAME")) {
            lst[9] = value;
            return lst;
        } else if (flag.equals("STANDARD_APP2_COD")) {
            lst[10] = value;
            return lst;
        } else if (flag.equals("STANDARD_PACK_DES")) {
            lst[11] = value;
            return lst;
        } else if (flag.equals("PACK_NUMBER2")) {
            lst[12] = value;
            return lst;
        } else if (flag.equals("STANDARD_CORP_NAME")) {
            lst[13] = value;
            return lst;
        } else if (flag.equals("min2")) {
            lst[14] = value;
            return lst;
        } else if (flag.equals("PACK_ID")) {
            lst[15] = value;
            return lst;
        } else {
            return lst;
        }
    }

    @Override
    protected String expendTitle(String value) {
        return value;
    }

    @Override
    protected String prePanelFunction(String value) {
        String[] lst = splitValues(value);

        if("".equals(getCellKey(lst, "PACK_NUMBER2")))
            lst = setCellKey(lst, "PACK_NUMBER2", getCellKey(lst, "PACK_NUMBER1"));
        if("抗人胸腺细胞兔免疫球蛋白".equals(getCellKey(lst, "STANDARD_MOLE_NAME")))
            lst = setCellKey(lst, "STANDARD_MOLE_NAME", "抗人胸腺细胞免疫球蛋白");
        if("米芙".equals(getCellKey(lst, "STANDARD_PRODUCT_NAME")))
            lst = setCellKey(lst, "STANDARD_MOLE_NAME", "麦考芬酸钠");
        if("阿洛刻".equals(getCellKey(lst, "STANDARD_PRODUCT_NAME"))){
            lst = setCellKey(setCellKey(lst, "STANDARD_MOLE_NAME", "奥洛他定"), "STANDARD_CORP_NAME", "安斯泰来制药集团");
        }
        if("乌拉地尔".equals(getCellKey(lst, "STANDARD_PRODUCT_NAME")))
            lst = setCellKey(lst, "STANDARD_MOLE_NAME", "乌拉地尔");
        if("坦亮".equals(getCellKey(lst, "STANDARD_PRODUCT_NAME")))
            lst = setCellKey(lst, "STANDARD_MOLE_NAME", "苯磺贝他斯汀");
        if(getCellKey(lst, "STANDARD_CORP_NAME").startsWith("扬子江") && getCellKey(lst, "STANDARD_PRODUCT_NAME").equals("地氯雷他定"))
            lst = setCellKey(lst, "STANDARD_PRODUCT_NAME", "贝雪");
        if(getCellKey(lst, "STANDARD_CORP_NAME").startsWith("杭州朱") && getCellKey(lst, "STANDARD_PRODUCT_NAME").equals("非布司他"))
            lst = setCellKey(lst, "STANDARD_PRODUCT_NAME", "风定宁");
        if(getCellKey(lst, "STANDARD_CORP_NAME").startsWith("江苏恒") && getCellKey(lst, "STANDARD_PRODUCT_NAME").equals("非布司他"))
            lst = setCellKey(lst, "STANDARD_PRODUCT_NAME", "瑞扬");
        if(getCellKey(lst, "STANDARD_CORP_NAME").startsWith("江苏万") && getCellKey(lst, "STANDARD_PRODUCT_NAME").equals("非布司他"))
            lst = setCellKey(lst, "STANDARD_PRODUCT_NAME", "优立通");
        if(getCellKey(lst, "STANDARD_CORP_NAME").startsWith("扬子江") && getCellKey(lst, "STANDARD_MOLE_NAME").equals("地氯雷他定"))
            lst = setCellKey(lst, "STANDARD_MOLE_NAME", "枸地氯雷他定");
        if("新哈乐".equals(getCellKey(lst, "STANDARD_PRODUCT_NAME")))
            lst = setCellKey(lst, "STANDARD_MOLE_NAME", "坦索罗辛");

        String min2 = getCellKey(lst, "STANDARD_PRODUCT_NAME") + "|" +
                getCellKey(lst, "STANDARD_APP2_COD") + "|" +
                getCellKey(lst, "STANDARD_PACK_DES") + "|" +
                getCellKey(lst, "PACK_NUMBER2") + "|" +
                getCellKey(lst, "STANDARD_CORP_NAME");
        lst = setCellKey(lst, "min2", min2);

        if("哈乐".equals(getCellKey(lst, "STANDARD_PRODUCT_NAME")) &&
                "片剂".equals(getCellKey(lst, "STANDARD_APP2_COD")) &&
                "14".equals(getCellKey(lst, "PACK_NUMBER2")))
            lst = setCellKey(lst, "STANDARD_PRODUCT_NAME", "新哈乐");
        if("新哈乐".equals(getCellKey(lst, "STANDARD_PRODUCT_NAME")) &&
                "片剂".equals(getCellKey(lst, "STANDARD_APP2_COD")) &&
                "10".equals(getCellKey(lst, "PACK_NUMBER2")))
            lst = setCellKey(lst, "STANDARD_PRODUCT_NAME", "哈乐");

        return mkString(lst, delimiter);
    }

}
