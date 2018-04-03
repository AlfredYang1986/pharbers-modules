package com.pharbers.panel.astellas

import com.pharbers.pactions.actionbase._
import com.pharbers.panel.format.input.writable.astellas._

/**
  * Created by spark on 18-3-27.
  */
object phAstellasPanelImplAction  {
    def apply(company: String, ym: List[String], mkt: String) : pActionTrait = {
        val tmp = new phAstellasPanelImplAction(company, ym, mkt)
        tmp.name = "panelResult"
        tmp
    }
}

class phAstellasPanelImplAction(company: String, ym: List[String], mkt: String) extends pActionTrait {

    val delimiter = 31.toChar.toString
    override val defaultArgs: pActionArgs = NULLArgs
    override implicit def progressFunc(progress : Double, flag : String) : Unit = {}

    override def perform(pr : pActionArgs)(implicit f : (Double, String) => Unit) : pActionArgs = {
        // 1. 读入cpa原始数据 2.在Writable进行预处理
        val dataMap = pr.asInstanceOf[MapArgs].get
        val cpa0 = dataMap("cpa").asInstanceOf[RDDArgs[phAstellasCpaWritable]].get
        val gycx0 = dataMap("gycx").asInstanceOf[RDDArgs[phAstellasGycxWritable]].get
        val markets_match = dataMap("markets_match_file").asInstanceOf[RDDArgs[phAstellasMarketsMatchWritable]].get
        val product_match = dataMap("product_match_file").asInstanceOf[RDDArgs[phAstellasProductMatchWritable]].get
        val universe = dataMap("universe_file").asInstanceOf[RDDArgs[phAstellasUniverseWritable]].get
        val hospital = dataMap("hospital_file").asInstanceOf[RDDArgs[phAstellasHospitalWritable]].get

        val hospital_cpa =
            hospital.filter(_.getRowKey("CPA_DIS") == " ")
                .filter(x => x.getRowKey("CPA_CODE") != "" && x.getRowKey("CPA_CODE") != " ")
                .map { iter => iter.getRowKey("CPA_CODE") -> iter }
        val hospital_gycx =
            hospital.filter(_.getRowKey("GYC_DIS") == " ")
                .filter(x => x.getRowKey("GYC_CODE") != "" && x.getRowKey("GYC_CODE") != " ")
                .map { iter => iter.getRowKey("GYC_CODE") -> iter }

        val standard_cpa_code =
            cpa0.map { iter => iter.getRowKey("HOSPITAL_CODE") -> iter }
                .leftOuterJoin(hospital_cpa)
                .filter(_._2._2.isDefined)
                .map { iter =>
                    iter._2._2.get.getRowKey("STANDARD_CODE") + "_" + iter._2._1.getRowKey("YM") -> iter._2._1
                }

        val standard_gyc_code =
            gycx0.map { iter => iter.getRowKey("HOSPITAL_CODE") -> iter }
                .leftOuterJoin(hospital_gycx)
                .filter(_._2._2.isDefined)
                .map { iter =>
                    iter._2._2.get.getRowKey("STANDARD_CODE") + "_" + iter._2._1.getRowKey("YM") -> iter._2._1
                }

        val double_hosp_code = standard_cpa_code.map(_._1 -> 1).intersection(standard_gyc_code.map(_._1 -> 1))


        //3. GYCX匹配市场
        val markets_match1 = markets_match.map { iter => (iter.getRowKey("MOLE_NAME"), iter.getRowKey("MARKET")) } // mkt.MOLE_NAME -> mkt.MARKET

        val cpa1 = cpa0.map { iter => iter.getRowKey("min1") -> iter } // cpa.min1 -> cpaRDD

        val gycx1 =
            standard_gyc_code.leftOuterJoin(double_hosp_code)
                    .filter(_._2._2.isEmpty)
                    .map { iter => iter._2._1.getRowKey("MOLE_NAME") -> iter._2._1 }
                    .join(markets_match1)
                .map{ iter =>
                    iter._2._1.getRowKey("min1") -> (iter._2._1, iter._2._2)
                } // gycx.min1 -> (gycxRDD, gycx.market)


        //4. min1匹配  5. 修改市场
        val product_match1 = product_match.map { iter => iter.getRowKey("min0") -> iter } // product.min0 -> productRDD

        val cpa2 =
            cpa1.join(product_match1) // min1 -> (cpaRDD, productRDD)
                    .map { iter =>

                val market = if (iter._2._2.getRowKey("STANDARD_MOLE_NAME") == "他克莫司" && iter._2._2.getRowKey("STANDARD_APP2_COD") == "软膏剂")
                    "普特彼市场"
                else if (iter._2._2.getRowKey("STANDARD_MOLE_NAME") == "他克莫司" && iter._2._2.getRowKey("STANDARD_APP2_COD") != "软膏剂")
                    "普乐可复市场"
                else
                    iter._2._1.getRowKey("MARKET")

                market -> iter._2
            } // market -> (cpaRDD, productRDD)

        val gycx2 =
            gycx1.join(product_match1) // min1 -> ((gycxRDD, gycx.market), productRDD)
                .map { iter =>

            val market = if (iter._2._2.getRowKey("STANDARD_MOLE_NAME") == "他克莫司" && iter._2._2.getRowKey("STANDARD_APP2_COD") == "软膏剂")
                "普特彼市场"
            else if (iter._2._2.getRowKey("STANDARD_MOLE_NAME") == "他克莫司" && iter._2._2.getRowKey("STANDARD_APP2_COD") != "软膏剂")
                "普乐可复市场"
            else
                iter._2._1._2

            market -> (iter._2._1._1, iter._2._2)
        } // market -> (gycxRDD, productRDD)


        //6. 删除一些数据
        val cpa3 = cpa2.filter(iter => !(iter._1 == "佩尔市场" && (iter._2._2.getRowKey("STANDARD_APP2_COD") != "粉针剂" && iter._2._2.getRowKey("STANDARD_APP2_COD") != "注射剂")))
                .filter(iter => !(iter._1 == "阿洛刻市场" && iter._2._2.getRowKey("STANDARD_APP2_COD") == "粉针剂"))
                .filter(iter => !(iter._1 == "阿洛刻市场" && iter._2._2.getRowKey("STANDARD_APP2_COD") == "注射剂"))
                .filter(iter => !(iter._1 == "阿洛刻市场" && iter._2._2.getRowKey("STANDARD_APP2_COD") == "滴眼剂"))
                .filter(iter => !(iter._1 == "米开民市场" && iter._2._2.getRowKey("STANDARD_APP2_COD") == "颗粒剂"))
                .filter(iter => !(iter._1 == "米开民市场" && iter._2._2.getRowKey("STANDARD_APP2_COD") == "胶囊剂"))
                .filter(iter => !(iter._1 == "米开民市场" && iter._2._2.getRowKey("STANDARD_APP2_COD") == "滴眼剂"))
                .filter(iter => !(iter._1 == "米开民市场" && iter._2._2.getRowKey("STANDARD_APP2_COD") == "口服溶剂"))
                .filter(iter => !(iter._1 == "米开民市场" && iter._2._2.getRowKey("STANDARD_APP2_COD") == "片剂"))
                .filter(iter => !(iter._1 == "普乐可复市场" && iter._2._2.getRowKey("STANDARD_APP2_COD") == "滴眼剂"))
                .filter(iter => !(iter._2._2.getRowKey("STANDARD_PRODUCT_NAME") == "保法止"))
                .filter(iter => !(iter._2._2.getRowKey("min2") == "先立晓|片剂|1MG|10|浙江仙琚制药股份有限公司"))
                .filter(iter => !(iter._2._2.getRowKey("STANDARD_MOLE_NAME") == "倍他司汀"))
                .filter(iter => !(iter._2._2.getRowKey("STANDARD_MOLE_NAME") == "阿魏酰γ-丁二胺/植物生长素"))
                .filter(iter => !(iter._2._2.getRowKey("STANDARD_MOLE_NAME") == "丙磺舒"))
                .filter(iter => !(iter._2._2.getRowKey("STANDARD_MOLE_NAME") == "复方别嘌醇"))

        val gycx3 = gycx2.filter(iter => !(iter._1 == "佩尔市场" && (iter._2._2.getRowKey("STANDARD_APP2_COD") != "粉针剂" && iter._2._2.getRowKey("STANDARD_APP2_COD") != "注射剂")))
                .filter(iter => !(iter._1 == "阿洛刻市场" && iter._2._2.getRowKey("STANDARD_APP2_COD") == "粉针剂"))
                .filter(iter => !(iter._1 == "阿洛刻市场" && iter._2._2.getRowKey("STANDARD_APP2_COD") == "注射剂"))
                .filter(iter => !(iter._1 == "阿洛刻市场" && iter._2._2.getRowKey("STANDARD_APP2_COD") == "滴眼剂"))
                .filter(iter => !(iter._1 == "米开民市场" && iter._2._2.getRowKey("STANDARD_APP2_COD") == "颗粒剂"))
                .filter(iter => !(iter._1 == "米开民市场" && iter._2._2.getRowKey("STANDARD_APP2_COD") == "胶囊剂"))
                .filter(iter => !(iter._1 == "米开民市场" && iter._2._2.getRowKey("STANDARD_APP2_COD") == "滴眼剂"))
                .filter(iter => !(iter._1 == "米开民市场" && iter._2._2.getRowKey("STANDARD_APP2_COD") == "口服溶剂"))
                .filter(iter => !(iter._1 == "米开民市场" && iter._2._2.getRowKey("STANDARD_APP2_COD") == "片剂"))
                .filter(iter => !(iter._1 == "普乐可复市场" && iter._2._2.getRowKey("STANDARD_APP2_COD") == "滴眼剂"))
                .filter(iter => !(iter._2._2.getRowKey("STANDARD_PRODUCT_NAME") == "保法止"))
                .filter(iter => !(iter._2._2.getRowKey("min2") == "先立晓|片剂|1MG|10|浙江仙琚制药股份有限公司"))
                .filter(iter => !(iter._2._2.getRowKey("STANDARD_MOLE_NAME") == "倍他司汀"))
                .filter(iter => !(iter._2._2.getRowKey("STANDARD_MOLE_NAME") == "阿魏酰γ-丁二胺/植物生长素"))
                .filter(iter => !(iter._2._2.getRowKey("STANDARD_MOLE_NAME") == "丙磺舒"))
                .filter(iter => !(iter._2._2.getRowKey("STANDARD_MOLE_NAME") == "复方别嘌醇"))


        // 7. group 后 求和
        val cpa4 =
            cpa3.map { iter => // market -> (cpaRDD, productRDD)
                (iter._2._1.getRowKey("HOSPITAL_CODE"), iter._2._1.getRowKey("YM"), iter._2._2.getRowKey("min2"), iter._1) ->
                        (iter._2._1.getRowKey("VALUE").toDouble, iter._2._1.getRowKey("STANDARD_UNIT").toDouble)
            }.reduceByKey((p, n) => (p._1 + n._1, p._2 + n._2)).map { iter =>
                iter._1._1 -> (iter._1._2, iter._1._3, iter._1._4, iter._2._1, iter._2._2)
            } // HOSPITAL_CODE -> (YM, min2, mkt, values, units)

        val gycx4 =
            gycx3.map { iter => // market -> (cpaRDD, productRDD)
                (iter._2._1.getRowKey("HOSPITAL_CODE"), iter._2._1.getRowKey("YM"), iter._2._2.getRowKey("min2"), iter._1) ->
                        (iter._2._1.getRowKey("VALUE").toDouble, iter._2._1.getRowKey("STANDARD_UNIT").toDouble)
            }.reduceByKey((p, n) => (p._1 + n._1, p._2 + n._2)).map { iter =>
                iter._1._1 -> (iter._1._2, iter._1._3, iter._1._4, iter._2._1, iter._2._2)
            } // HOSPITAL_CODE -> (YM, min2, mkt, values, units)



        // 处理univers,只保留样本医院
        val universe1 =
            universe.filter(iter => iter.getRowKey("PANEL_ID") != "" && iter.getRowKey("PANEL_ID") != " ")
                    .map { iter =>
                        iter.getRowKey("PANEL_ID") -> iter.getRowKey("PHA_ID")
                    } // univers.PANEL_ID -> univers.PHA_ID

        val panel =
            (cpa4 union gycx4).leftOuterJoin(universe1)
                    .filter(_._2._2.isDefined)
                    .map { iter =>
                        (iter._1, iter._2._1._1, iter._2._1._2, iter._2._1._3, iter._2._2.get, iter._2._1._4, iter._2._1._5)
                    }
                    .filter(_._5 != "")
                    .map { iter =>
                        iter._1 + delimiter + iter._2 + delimiter + iter._3 + delimiter +
                                iter._4 + delimiter + iter._5 + delimiter + iter._6 + delimiter + iter._7
                    }

        RDDArgs(panel)
    }


}