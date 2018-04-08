package com.pharbers.panel.astellas

import org.apache.spark.sql.DataFrame
import com.pharbers.spark.phSparkDriver
import com.pharbers.pactions.actionbase._
import org.apache.spark.sql.types.{DoubleType, LongType}

object phAstellasPanelConcretJob {
    def apply(args: MapArgs): pActionTrait = new phAstellasPanelConcretJob(args)
}

class phAstellasPanelConcretJob(override val defaultArgs : pActionArgs) extends pActionTrait {
    override val name: String = "panel"
    override implicit def progressFunc(progress : Double, flag : String) : Unit = {}

    lazy val sparkDriver: phSparkDriver = phSparkDriver()

    override def perform(args : pActionArgs)(implicit f: (Double, String) => Unit) : pActionArgs = {

        val ym = defaultArgs.asInstanceOf[MapArgs].get("ym").asInstanceOf[StringArgs].get
        val mkt = defaultArgs.asInstanceOf[MapArgs].get("mkt").asInstanceOf[StringArgs].get

        val cpa = args.asInstanceOf[MapArgs].get("cpa").asInstanceOf[DFArgs].get
        val gycx = args.asInstanceOf[MapArgs].get("gycx").asInstanceOf[DFArgs].get
        val product_match_file = args.asInstanceOf[MapArgs].get("product_match_file").asInstanceOf[DFArgs].get
        val markets_match = args.asInstanceOf[MapArgs].get("markets_match_file").asInstanceOf[DFArgs].get
        val universe_file = args.asInstanceOf[MapArgs].get("universe_file").asInstanceOf[DFArgs].get
        val hospital = args.asInstanceOf[MapArgs].get("hospital_file").asInstanceOf[DFArgs].get


        val delete_double_gycx: DataFrame = {
            val hospital_cpa = hospital.filter(s" CPA_DIS == ' ' OR CPA_DIS == '' ")
                    .filter(s" CPA_CODE != ' ' AND CPA_CODE != '' ")
            val hospital_gyc = hospital.filter(s" GYC_DIS == ' ' OR GYC_DIS == '' ")
                    .filter(s" GYC_CODE != ' ' AND GYC_CODE != '' ")

            val standard_cpa_code = cpa.join(hospital_cpa, cpa("HOSPITAL_CODE") === hospital_cpa("CPA_CODE"))
                    .selectExpr("concat(YM, '_', STANDARD_CODE) as JR_CODE")
            val standard_gyc_code = gycx.join(hospital_gyc, gycx("HOSPITAL_CODE") === hospital_gyc("GYC_CODE"))
                    .selectExpr("concat(YM, '_', STANDARD_CODE) as JR_CODE")
/////////////////////////////////////////////////////////
            standard_cpa_code.show(false)

            val double = standard_cpa_code
                    .intersect(standard_gyc_code.select("JR_CODE"))
                    .withColumnRenamed("JR_CODE", "ID")

            standard_gyc_code.join(double, standard_gyc_code("JR_CODE") === double("ID"), "left")
                    .filter("ID is null").drop("ID")
        }
        delete_double_gycx.show(false)


//        //3. GYCX匹配市场
//        val markets_match1 = markets_match.map { iter => (iter.getRowKey("MOLE_NAME"), iter.getRowKey("MARKET")) } // mkt.MOLE_NAME -> mkt.MARKET
//
//        val cpa1 = cpa0.map { iter => iter.getRowKey("min1") -> iter } // cpa.min1 -> cpaRDD
//
//        val gycx1 =
//            standard_gyc_code.leftOuterJoin(double_hosp_code)
//                    .filter(_._2._2.isEmpty)
//                    .map { iter => iter._2._1.getRowKey("MOLE_NAME") -> iter._2._1 }
//                    .join(markets_match1)
//                    .map{ iter =>
//                        iter._2._1.getRowKey("min1") -> (iter._2._1, iter._2._2)
//                    } // gycx.min1 -> (gycxRDD, gycx.market)
//
//
//        //4. min1匹配  5. 修改市场
//        val product_match1 = product_match.map { iter => iter.getRowKey("min0") -> iter } // product.min0 -> productRDD
//
//        val cpa2 =
//            cpa1.join(product_match1) // min1 -> (cpaRDD, productRDD)
//                    .map { iter =>
//
//                val market = if (iter._2._2.getRowKey("STANDARD_MOLE_NAME") == "他克莫司" && iter._2._2.getRowKey("STANDARD_APP2_COD") == "软膏剂")
//                    "普特彼市场"
//                else if (iter._2._2.getRowKey("STANDARD_MOLE_NAME") == "他克莫司" && iter._2._2.getRowKey("STANDARD_APP2_COD") != "软膏剂")
//                    "普乐可复市场"
//                else
//                    iter._2._1.getRowKey("MARKET")
//
//                market -> iter._2
//            } // market -> (cpaRDD, productRDD)
//
//        val gycx2 =
//            gycx1.join(product_match1) // min1 -> ((gycxRDD, gycx.market), productRDD)
//                    .map { iter =>
//
//                val market = if (iter._2._2.getRowKey("STANDARD_MOLE_NAME") == "他克莫司" && iter._2._2.getRowKey("STANDARD_APP2_COD") == "软膏剂")
//                    "普特彼市场"
//                else if (iter._2._2.getRowKey("STANDARD_MOLE_NAME") == "他克莫司" && iter._2._2.getRowKey("STANDARD_APP2_COD") != "软膏剂")
//                    "普乐可复市场"
//                else
//                    iter._2._1._2
//
//                market -> (iter._2._1._1, iter._2._2)
//            } // market -> (gycxRDD, productRDD)
//
//
//        //6. 删除一些数据
//        val cpa3 = cpa2.filter(iter => !(iter._1 == "佩尔市场" && (iter._2._2.getRowKey("STANDARD_APP2_COD") != "粉针剂" && iter._2._2.getRowKey("STANDARD_APP2_COD") != "注射剂")))
//                .filter(iter => !(iter._1 == "阿洛刻市场" && iter._2._2.getRowKey("STANDARD_APP2_COD") == "粉针剂"))
//                .filter(iter => !(iter._1 == "阿洛刻市场" && iter._2._2.getRowKey("STANDARD_APP2_COD") == "注射剂"))
//                .filter(iter => !(iter._1 == "阿洛刻市场" && iter._2._2.getRowKey("STANDARD_APP2_COD") == "滴眼剂"))
//                .filter(iter => !(iter._1 == "米开民市场" && iter._2._2.getRowKey("STANDARD_APP2_COD") == "颗粒剂"))
//                .filter(iter => !(iter._1 == "米开民市场" && iter._2._2.getRowKey("STANDARD_APP2_COD") == "胶囊剂"))
//                .filter(iter => !(iter._1 == "米开民市场" && iter._2._2.getRowKey("STANDARD_APP2_COD") == "滴眼剂"))
//                .filter(iter => !(iter._1 == "米开民市场" && iter._2._2.getRowKey("STANDARD_APP2_COD") == "口服溶剂"))
//                .filter(iter => !(iter._1 == "米开民市场" && iter._2._2.getRowKey("STANDARD_APP2_COD") == "片剂"))
//                .filter(iter => !(iter._1 == "普乐可复市场" && iter._2._2.getRowKey("STANDARD_APP2_COD") == "滴眼剂"))
//                .filter(iter => !(iter._2._2.getRowKey("STANDARD_PRODUCT_NAME") == "保法止"))
//                .filter(iter => !(iter._2._2.getRowKey("min2") == "先立晓|片剂|1MG|10|浙江仙琚制药股份有限公司"))
//                .filter(iter => !(iter._2._2.getRowKey("STANDARD_MOLE_NAME") == "倍他司汀"))
//                .filter(iter => !(iter._2._2.getRowKey("STANDARD_MOLE_NAME") == "阿魏酰γ-丁二胺/植物生长素"))
//                .filter(iter => !(iter._2._2.getRowKey("STANDARD_MOLE_NAME") == "丙磺舒"))
//                .filter(iter => !(iter._2._2.getRowKey("STANDARD_MOLE_NAME") == "复方别嘌醇"))
//
//        val gycx3 = gycx2.filter(iter => !(iter._1 == "佩尔市场" && (iter._2._2.getRowKey("STANDARD_APP2_COD") != "粉针剂" && iter._2._2.getRowKey("STANDARD_APP2_COD") != "注射剂")))
//                .filter(iter => !(iter._1 == "阿洛刻市场" && iter._2._2.getRowKey("STANDARD_APP2_COD") == "粉针剂"))
//                .filter(iter => !(iter._1 == "阿洛刻市场" && iter._2._2.getRowKey("STANDARD_APP2_COD") == "注射剂"))
//                .filter(iter => !(iter._1 == "阿洛刻市场" && iter._2._2.getRowKey("STANDARD_APP2_COD") == "滴眼剂"))
//                .filter(iter => !(iter._1 == "米开民市场" && iter._2._2.getRowKey("STANDARD_APP2_COD") == "颗粒剂"))
//                .filter(iter => !(iter._1 == "米开民市场" && iter._2._2.getRowKey("STANDARD_APP2_COD") == "胶囊剂"))
//                .filter(iter => !(iter._1 == "米开民市场" && iter._2._2.getRowKey("STANDARD_APP2_COD") == "滴眼剂"))
//                .filter(iter => !(iter._1 == "米开民市场" && iter._2._2.getRowKey("STANDARD_APP2_COD") == "口服溶剂"))
//                .filter(iter => !(iter._1 == "米开民市场" && iter._2._2.getRowKey("STANDARD_APP2_COD") == "片剂"))
//                .filter(iter => !(iter._1 == "普乐可复市场" && iter._2._2.getRowKey("STANDARD_APP2_COD") == "滴眼剂"))
//                .filter(iter => !(iter._2._2.getRowKey("STANDARD_PRODUCT_NAME") == "保法止"))
//                .filter(iter => !(iter._2._2.getRowKey("min2") == "先立晓|片剂|1MG|10|浙江仙琚制药股份有限公司"))
//                .filter(iter => !(iter._2._2.getRowKey("STANDARD_MOLE_NAME") == "倍他司汀"))
//                .filter(iter => !(iter._2._2.getRowKey("STANDARD_MOLE_NAME") == "阿魏酰γ-丁二胺/植物生长素"))
//                .filter(iter => !(iter._2._2.getRowKey("STANDARD_MOLE_NAME") == "丙磺舒"))
//                .filter(iter => !(iter._2._2.getRowKey("STANDARD_MOLE_NAME") == "复方别嘌醇"))
//
//
//        // 7. group 后 求和
//        val cpa4 =
//            cpa3.filter(_._2._1.getRowKey("HOSPITAL_CODE") != "HOSPITAL_CODE")
//                    .map { iter => // market -> (cpaRDD, productRDD)
//                        (iter._2._1.getRowKey("HOSPITAL_CODE").toLong, iter._2._1.getRowKey("YM"), iter._2._2.getRowKey("min2"), iter._1) ->
//                                (iter._2._1.getRowKey("VALUE").toDouble, iter._2._1.getRowKey("STANDARD_UNIT").toDouble)
//                    }.reduceByKey((p, n) => (p._1 + n._1, p._2 + n._2)).map { iter =>
//                iter._1._1 -> (iter._1._2, iter._1._3, iter._1._4, iter._2._1, iter._2._2)
//            } // HOSPITAL_CODE -> (YM, min2, mkt, values, units)
//
//        val gycx4 =
//            gycx3.filter(_._2._1.getRowKey("HOSPITAL_CODE") != "HOSPITAL_CODE")
//                    .map { iter => // market -> (cpaRDD, productRDD)
//                        (iter._2._1.getRowKey("HOSPITAL_CODE").toLong, iter._2._1.getRowKey("YM"), iter._2._2.getRowKey("min2"), iter._1) ->
//                                (iter._2._1.getRowKey("VALUE").toDouble, iter._2._1.getRowKey("STANDARD_UNIT").toDouble)
//                    }.reduceByKey((p, n) => (p._1 + n._1, p._2 + n._2)).map { iter =>
//                iter._1._1 -> (iter._1._2, iter._1._3, iter._1._4, iter._2._1, iter._2._2)
//            } // HOSPITAL_CODE -> (YM, min2, mkt, values, units)
//
//
//
//        // 处理univers,只保留样本医院
//        val universe1 =
//            universe.filter(iter => iter.getRowKey("PANEL_ID") != "" && iter.getRowKey("PANEL_ID") != " ")
//                    .filter(_.getRowKey("PANEL_ID") != "PANEL_ID").map { iter =>
//                iter.getRowKey("PANEL_ID").toLong -> iter.getRowKey("PHA_ID")
//            } // univers.PANEL_ID -> univers.PHA_ID
//
//        val total = cpa4 union gycx4
//
//        val panel0 = total.leftOuterJoin(universe1)
//        val panel =
//
//            panel0.filter(_._2._2.isDefined)
//                    .map { iter =>
//                        (iter._1, iter._2._1._1, iter._2._1._2, iter._2._1._3, iter._2._2.get, iter._2._1._4, iter._2._1._5)
//                    }
//                    .filter(iter => iter._5 != "" || iter._5 != " ")
//                    .map { iter =>
//                        iter._1 + delimiter + iter._2 + delimiter + iter._3 + delimiter +
//                                iter._4 + delimiter + iter._5 + delimiter + iter._6 + delimiter + iter._7
//                    }



        def getPanelFile(ym: String, mkt: String) : pActionArgs = {

//            val full_cpa = fullCPA(cpa, ym)
            val product_match = trimProductMatch(product_match_file)
            val universe = trimUniverse(universe_file, mkt)
            val markets_product_match = product_match.join(markets_match, markets_match("通用名_原始") === product_match("通用名"))
//            val filted_panel = full_cpa.join(universe, full_cpa("HOSPITAL_CODE") === universe("ID"))
//            val panelDF = trimPanel(filted_panel, markets_product_match)
            sparkDriver.sc.stop()
            DFArgs(???)
        }


        def trimProductMatch(product_match_file: DataFrame): DataFrame = {

            product_match_file
                .withColumnRenamed("药品名称", "NAME")
                .withColumnRenamed("商品名", "PRODUCT_NAME")
                .withColumnRenamed("剂型", "APP2_COD")
                .withColumnRenamed("规格", "PACK_DES")
                .withColumnRenamed("包装数量", "PACK_NUMBER")
                .withColumnRenamed("生产企业", "CORP_NAME")
                .withColumnRenamed("min1_标准", "min2")
                .selectExpr("concat(PRODUCT_NAME,APP2_COD,PACK_DES,PACK_NUMBER,CORP_NAME) as min1", "min2", "NAME")
                .withColumnRenamed("min2", "min1_标准")
                .withColumnRenamed("NAME", "通用名")
                .distinct()
        }

        def trimUniverse(universe_file: DataFrame, mkt: String): DataFrame = {

            import sparkDriver.ss.implicits._
            universe_file.withColumnRenamed("样本医院编码", "ID")
                .withColumnRenamed("PHA医院名称", "HOSP_NAME")
                .withColumnRenamed("PHA ID", "HOSP_ID")
                .withColumnRenamed("市场", "DOI")
                .withColumnRenamed("If Panel_All", "SAMPLE")
                .filter("SAMPLE like '1'")
                .selectExpr("ID", "HOSP_NAME", "HOSP_ID", "DOI", "DOI as DOIE")
                .filter(s"DOI like '$mkt'")
                .withColumn("ID", 'ID.cast(LongType))
        }

        def trimPanel(filted_panel: DataFrame, markets_product_match: DataFrame): DataFrame = {
            import sparkDriver.ss.implicits._
            val temp = filted_panel.join(markets_product_match, filted_panel("min1") === markets_product_match("min1"))
                .withColumn("ID", 'ID.cast(LongType))
                .withColumnRenamed("HOSP_NAME", "Hosp_name")
                .withColumnRenamed("YM", "Date")
                .withColumnRenamed("min1_标准", "Prod_Name")
                .withColumn("VALUE", 'VALUE.cast(DoubleType))
                .withColumnRenamed("VALUE", "Sales")
                .withColumn("STANDARD_UNIT", 'STANDARD_UNIT.cast(DoubleType))
                .withColumnRenamed("STANDARD_UNIT", "Units")
                .selectExpr("ID", "Hosp_name", "Date",
                    "Prod_Name", "Prod_Name as Prod_CNAME", "HOSP_ID", "Prod_Name as Strength",
                    "DOI", "DOIE", "Units", "Sales")

            temp.groupBy("ID", "Hosp_name", "Date", "Prod_Name", "Prod_CNAME", "HOSP_ID", "Strength", "DOI", "DOIE")
                .agg(Map("Units" -> "sum", "Sales" -> "sum"))
                .withColumnRenamed("sum(Units)", "Units")
                .withColumnRenamed("sum(Sales)", "Sales")
        }

//        getPanelFile(ym, mkt)
        ???
    }

}