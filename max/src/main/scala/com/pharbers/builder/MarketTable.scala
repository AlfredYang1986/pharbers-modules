package com.pharbers.builder

trait MarketTable {

    val nhwa_mz = Map(
        "company" -> "5afa53bded925c05c6f69c54",
        "subsidiary" -> "恩华",
        "market" -> "麻醉市场",
        "ymInstance" -> "com.pharbers.panel.nhwa.phNhwaCalcYMJob",
        "panelInstance" -> "com.pharbers.panel.nhwa.phNhwaPanelJob",
        "maxInstance" -> "com.pharbers.calc.phMaxJob",

        "source" -> "cpa",
        "panelArgs" -> "not_published_hosp_file#universe_file#product_match_file#fill_hos_data_file#markets_match_file",
        "not_published_hosp_file" -> "nhwa/2017年未出版医院名单.xlsx",
        "universe_file" -> "nhwa/universe_麻醉市场_online.xlsx",
        "product_match_file" -> "nhwa/nhwa匹配表.xlsx",
        "fill_hos_data_file" -> "nhwa/补充医院.xlsx",
        "markets_match_file" -> "nhwa/通用名市场定义.xlsx"
    )

    val astellas_alk = Map(
        "company" -> "5b023787810c6e0268fe6ff6",
        "subsidiary" -> "安斯泰来",
        "market" -> "阿洛刻市场",
        "ymInstance" -> "com.pharbers.panel.astellas.phAstellasCalcYMJob",
        "panelInstance" -> "com.pharbers.panel.astellas.phAstellasPanelJob",
        "maxInstance" -> "com.pharbers.calc.phMaxJob",

        "source" -> "cpa#gycx",
        "panelArgs" -> "universe_file#product_match_file#markets_match_file#hospital_file",
        "universe_file" -> "astellas/UNIVERSE_Allelock_online.xlsx",
        "product_match_file" -> "astellas/20171018药品最小单位IMS packid匹配表.xlsx",
        "markets_match_file" -> "astellas/20170203药品名称匹配市场.xlsx",
        "hospital_file" -> "astellas/医院名称编码等级三源互匹20180314.xlsx"
    )

    val astellas_mkm = Map(
        "company" -> "5b023787810c6e0268fe6ff6",
        "subsidiary" -> "安斯泰来",
        "market" -> "米开民市场",
        "ymInstance" -> "com.pharbers.panel.astellas.phAstellasCalcYMJob",
        "panelInstance" -> "com.pharbers.panel.astellas.phAstellasPanelJob",
        "maxInstance" -> "com.pharbers.calc.phMaxJob",

        "source" -> "cpa#gycx",
        "panelArgs" -> "universe_file#product_match_file#markets_match_file#hospital_file",
//        "universe_file" -> "astellas/UNIVERSE_Mycamine_online.xlsx",
        "universe_file" ->  "astellas/UNIVERSE_Allelock_online.xlsx",
        "product_match_file" -> "astellas/20171018药品最小单位IMS packid匹配表.xlsx",
        "markets_match_file" -> "astellas/20170203药品名称匹配市场.xlsx",
        "hospital_file" -> "astellas/医院名称编码等级三源互匹20180314.xlsx"
    )

    val astellas_plkf = Map(
        "company" -> "5b023787810c6e0268fe6ff6",
        "subsidiary" -> "安斯泰来",
        "market" -> "普乐可复市场",
        "ymInstance" -> "com.pharbers.panel.astellas.phAstellasCalcYMJob",
        "panelInstance" -> "com.pharbers.panel.astellas.phAstellasPanelJob",
        "maxInstance" -> "com.pharbers.calc.phMaxJob",

        "source" -> "cpa#gycx",
        "panelArgs" -> "universe_file#product_match_file#markets_match_file#hospital_file",
//        "universe_file" -> "astellas/UNIVERSE_Prograf_online.xlsx",
        "universe_file" ->  "astellas/UNIVERSE_Allelock_online.xlsx",
        "product_match_file" -> "astellas/20171018药品最小单位IMS packid匹配表.xlsx",
        "markets_match_file" -> "astellas/20170203药品名称匹配市场.xlsx",
        "hospital_file" -> "astellas/医院名称编码等级三源互匹20180314.xlsx"
    )

    val astellas_pe = Map(
        "company" -> "5b023787810c6e0268fe6ff6",
        "subsidiary" -> "安斯泰来",
        "market" -> "佩尔市场",
        "ymInstance" -> "com.pharbers.panel.astellas.phAstellasCalcYMJob",
        "panelInstance" -> "com.pharbers.panel.astellas.phAstellasPanelJob",
        "maxInstance" -> "com.pharbers.calc.phMaxJob",

        "source" -> "cpa#gycx",
        "panelArgs" -> "universe_file#product_match_file#markets_match_file#hospital_file",
//        "universe_file" -> "astellas/UNIVERSE_Perdipine_online.xlsx",
        "universe_file" ->  "astellas/UNIVERSE_Allelock_online.xlsx",
        "product_match_file" -> "astellas/20171018药品最小单位IMS packid匹配表.xlsx",
        "markets_match_file" -> "astellas/20170203药品名称匹配市场.xlsx",
        "hospital_file" -> "astellas/医院名称编码等级三源互匹20180314.xlsx"
    )

    val astellas_hl = Map(
        "company" -> "5b023787810c6e0268fe6ff6",
        "subsidiary" -> "安斯泰来",
        "market" -> "哈乐市场",
        "ymInstance" -> "com.pharbers.panel.astellas.phAstellasCalcYMJob",
        "panelInstance" -> "com.pharbers.panel.astellas.phAstellasPanelJob",
        "maxInstance" -> "com.pharbers.calc.phMaxJob",

        "source" -> "cpa#gycx",
        "panelArgs" -> "universe_file#product_match_file#markets_match_file#hospital_file",
//        "universe_file" -> "astellas/UNIVERSE_Harnal_online.xlsx",
        "universe_file" ->  "astellas/UNIVERSE_Allelock_online.xlsx",
        "product_match_file" -> "astellas/20171018药品最小单位IMS packid匹配表.xlsx",
        "markets_match_file" -> "astellas/20170203药品名称匹配市场.xlsx",
        "hospital_file" -> "astellas/医院名称编码等级三源互匹20180314.xlsx"
    )

    val astellas_tf = Map(
        "company" -> "5b023787810c6e0268fe6ff6",
        "subsidiary" -> "安斯泰来",
        "market" -> "痛风市场",
        "ymInstance" -> "com.pharbers.panel.astellas.phAstellasCalcYMJob",
        "panelInstance" -> "com.pharbers.panel.astellas.phAstellasPanelJob",
        "maxInstance" -> "com.pharbers.calc.phMaxJob",

        "source" -> "cpa#gycx",
        "panelArgs" -> "universe_file#product_match_file#markets_match_file#hospital_file",
//        "universe_file" -> "astellas/UNIVERSE_Gout_online.xlsx",
        "universe_file" ->  "astellas/UNIVERSE_Allelock_online.xlsx",
        "product_match_file" -> "astellas/20171018药品最小单位IMS packid匹配表.xlsx",
        "markets_match_file" -> "astellas/20170203药品名称匹配市场.xlsx",
        "hospital_file" -> "astellas/医院名称编码等级三源互匹20180314.xlsx"
    )

    val astellas_wxk = Map(
        "company" -> "5b023787810c6e0268fe6ff6",
        "subsidiary" -> "安斯泰来",
        "market" -> "卫喜康市场",
        "ymInstance" -> "com.pharbers.panel.astellas.phAstellasCalcYMJob",
        "panelInstance" -> "com.pharbers.panel.astellas.phAstellasPanelJob",
        "maxInstance" -> "com.pharbers.calc.phMaxJob",

        "source" -> "cpa#gycx",
        "panelArgs" -> "universe_file#product_match_file#markets_match_file#hospital_file",
//        "universe_file" -> "astellas/UNIVERSE_Vesicare_online.xlsx",
        "universe_file" ->  "astellas/UNIVERSE_Allelock_online.xlsx",
        "product_match_file" -> "astellas/20171018药品最小单位IMS packid匹配表.xlsx",
        "markets_match_file" -> "astellas/20170203药品名称匹配市场.xlsx",
        "hospital_file" -> "astellas/医院名称编码等级三源互匹20180314.xlsx"
    )

    val astellas_Grafalon = Map(
        "company" -> "5b023787810c6e0268fe6ff6",
        "subsidiary" -> "安斯泰来",
        "market" -> "Grafalon市场",
        "ymInstance" -> "com.pharbers.panel.astellas.phAstellasCalcYMJob",
        "panelInstance" -> "com.pharbers.panel.astellas.phAstellasPanelJob",
        "maxInstance" -> "com.pharbers.calc.phMaxJob",

        "source" -> "cpa#gycx",
        "panelArgs" -> "universe_file#product_match_file#markets_match_file#hospital_file",
//        "universe_file" -> "astellas/UNIVERSE_Grafalon_online.xlsx",
        "universe_file" ->  "astellas/UNIVERSE_Allelock_online.xlsx",
        "product_match_file" -> "astellas/20171018药品最小单位IMS packid匹配表.xlsx",
        "markets_match_file" -> "astellas/20170203药品名称匹配市场.xlsx",
        "hospital_file" -> "astellas/医院名称编码等级三源互匹20180314.xlsx"
    )

    val marketTable: List[Map[String, String]] = nhwa_mz :: // 恩华公司
//            astellas_alk ::
            astellas_mkm ::
//            astellas_plkf ::
//            astellas_pe ::
//            astellas_hl ::
//            astellas_tf :: astellas_wxk :: astellas_Grafalon :: // 安斯泰来公司
            Nil

}
