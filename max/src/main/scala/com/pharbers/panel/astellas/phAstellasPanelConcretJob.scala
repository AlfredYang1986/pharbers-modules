package com.pharbers.panel.astellas

import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.{DoubleType, LongType}

import com.pharbers.spark.phSparkDriver
import com.pharbers.pactions.actionbase._

object phAstellasPanelConcretJob {
    def apply(args: MapArgs): pActionTrait = new phAstellasPanelConcretJob(args)
}

class phAstellasPanelConcretJob(override val defaultArgs: pActionArgs) extends pActionTrait {
    override val name: String = "panel"

    lazy val sparkDriver: phSparkDriver = phSparkDriver()

    import sparkDriver.ss.implicits._

    override def perform(args: pActionArgs): pActionArgs = {

        val ym = defaultArgs.asInstanceOf[MapArgs].get("ym").asInstanceOf[StringArgs].get
        val mkt = defaultArgs.asInstanceOf[MapArgs].get("mkt").asInstanceOf[StringArgs].get
        val mkt_en = defaultArgs.asInstanceOf[MapArgs].get("mkt_en").asInstanceOf[StringArgs].get

        val cpa = args.asInstanceOf[MapArgs].get("cpa").asInstanceOf[DFArgs].get.filter(col("YM") === ym)
        val gycx = args.asInstanceOf[MapArgs].get("gycx").asInstanceOf[DFArgs].get.filter(col("YM") === ym)
        val product_match_file = args.asInstanceOf[MapArgs].get("product_match_file").asInstanceOf[DFArgs].get
        val markets_match = args.asInstanceOf[MapArgs].get("markets_match_file").asInstanceOf[DFArgs].get
        val universe_file = args.asInstanceOf[MapArgs].get("universe_file").asInstanceOf[DFArgs].get
        val hospital = args.asInstanceOf[MapArgs].get("hospital_file").asInstanceOf[DFArgs].get


        // 去除GYC中的重复医院
        val delete_double_gycx: DataFrame = {
            val hospital_cpa = hospital.filter(s" CPA_DIS == ' ' OR CPA_DIS == '' ")
                .filter(s" CPA_CODE != ' ' AND CPA_CODE != '' ")
            val hospital_gyc = hospital.filter(s" GYC_DIS == ' ' OR GYC_DIS == '' ")
                .filter(s" GYC_CODE != ' ' AND GYC_CODE != '' ")

            val standard_cpa_code = cpa.join(hospital_cpa, cpa("HOSPITAL_CODE") === hospital_cpa("CPA_CODE"))
                .withColumn("JR_CODE", concat(col("YM"), col("STANDARD_CODE"))).select("JR_CODE")
            val standard_gyc_code = gycx.join(hospital_gyc, gycx("HOSPITAL_CODE") === hospital_gyc("GYC_CODE"))
                .withColumn("JR_CODE", concat(col("YM"), col("STANDARD_CODE")))

            val double = standard_cpa_code.intersect(standard_gyc_code.select("JR_CODE")).withColumnRenamed("JR_CODE", "ID")

            standard_gyc_code.join(double, standard_gyc_code("JR_CODE") === double("ID"), "left")
                .filter("ID is null").drop("ID", "JR_CODE")
                .withColumnRenamed("MOLE_NAME", "GYC_MOLE_NAME")
        }

        // 合并cpa和gycx的源数据
        val total = {
            val filtered_cpa = cpa.select("HOSPITAL_CODE", "YM", "MOLE_NAME", "min1", "MARKET", "VALUE", "STANDARD_UNIT")
            val have_market_gycx = delete_double_gycx.join(markets_match, delete_double_gycx("GYC_MOLE_NAME") === markets_match("MOLE_NAME"))
                .select(filtered_cpa.columns.head, filtered_cpa.columns.tail: _*).distinct()

            filtered_cpa.union(have_market_gycx)
                .withColumn("VALUE", 'VALUE.cast(DoubleType))
                .withColumnRenamed("VALUE", "Sales")
                .withColumn("STANDARD_UNIT", 'STANDARD_UNIT.cast(DoubleType))
                .withColumnRenamed("STANDARD_UNIT", "Units")
                .withColumn("HOSPITAL_CODE", 'HOSPITAL_CODE.cast(LongType))
                .filter(col("MARKET") === mkt)
        }

        val product_match = {
            product_match_file.select("min0", "min2", "STANDARD_MOLE_NAME", "STANDARD_APP2_COD", "STANDARD_PRODUCT_NAME")
                .distinct()
        }

        // 清洗市场
        val modifiedMarketTotal = {
            total.join(product_match, total("min1") === product_match("min0"))
                .withColumn("NEW_MARKET",
                    when(col("STANDARD_MOLE_NAME") === "他克莫司" && col("STANDARD_APP2_COD") === "软膏剂", "普特彼市场")
                        .when(col("STANDARD_MOLE_NAME") === "他克莫司" && col("STANDARD_APP2_COD") =!= "软膏剂", "普乐可复市场")
                        .otherwise(col("MARKET"))
                ).drop("MARKET").withColumnRenamed("NEW_MARKET", "MARKET")
        }

        // 删除一些数据
        val deletedTotal = {
            modifiedMarketTotal.filter(!(col("MARKET") === "佩尔市场" && (col("STANDARD_APP2_COD") =!= "粉针剂" && col("STANDARD_APP2_COD") =!= "注射剂")))
                .filter(!(col("MARKET") === "阿洛刻市场" && col("STANDARD_APP2_COD") === "粉针剂"))
                .filter(!(col("MARKET") === "阿洛刻市场" && col("STANDARD_APP2_COD") === "注射剂"))
                .filter(!(col("MARKET") === "阿洛刻市场" && col("STANDARD_APP2_COD") === "滴眼剂"))
                .filter(!(col("MARKET") === "米开民市场" && col("STANDARD_APP2_COD") === "颗粒剂"))
                .filter(!(col("MARKET") === "米开民市场" && col("STANDARD_APP2_COD") === "胶囊剂"))
                .filter(!(col("MARKET") === "米开民市场" && col("STANDARD_APP2_COD") === "滴眼剂"))
                .filter(!(col("MARKET") === "米开民市场" && col("STANDARD_APP2_COD") === "口服溶剂"))
                .filter(!(col("MARKET") === "米开民市场" && col("STANDARD_APP2_COD") === "片剂"))
                .filter(!(col("MARKET") === "普乐可复市场" && col("STANDARD_APP2_COD") === "滴眼剂"))
                .filter(!(col("STANDARD_PRODUCT_NAME") === "保法止"))
                .filter(!(col("min2") === "先立晓|片剂|1MG|10|浙江仙琚制药股份有限公司"))
                .filter(!(col("STANDARD_MOLE_NAME") === "倍他司汀"))
                .filter(!(col("STANDARD_MOLE_NAME") === "阿魏酰γ-丁二胺/植物生长素"))
                .filter(!(col("STANDARD_MOLE_NAME") === "丙磺舒"))
                .filter(!(col("STANDARD_MOLE_NAME") === "复方别嘌醇"))
        }

        // group 后 求和
        val groupedTotal = {
            deletedTotal.groupBy("HOSPITAL_CODE", "YM", "min2", "MARKET")
                .agg(Map("Units" -> "sum", "Sales" -> "sum"))
                .withColumnRenamed("sum(Units)", "Units")
                .withColumnRenamed("sum(Sales)", "Sales")
        }

        // 处理univers,只保留样本医院
        val universeCode = {
            universe_file.withColumnRenamed("样本医院编码", "PANLE_ID")
                .withColumnRenamed("PHA医院名称", "HOSP_NAME")
                .withColumnRenamed("PHA ID", "HOSP_ID")
                .filter(col("市场") === mkt_en)
                .select("PANLE_ID", "HOSP_ID", "HOSP_NAME")
                .filter(col("PANLE_ID") =!= "" && col("PANLE_ID") =!= " ")
                .withColumn("PANLE_ID", 'PANLE_ID.cast(LongType))
        }

        // 根据universeCode，只保留样本医院panel
        val panel = {
            groupedTotal.join(universeCode, groupedTotal("HOSPITAL_CODE") === universeCode("PANLE_ID"))
                .filter(col("MARKET") === mkt)
                .withColumn("ID", col("HOSPITAL_CODE").cast(LongType))
                .withColumn("Hosp_name", col("HOSP_NAME"))
                .withColumn("Date", col("YM"))
                .withColumn("Prod_Name", col("min2"))
                .withColumn("Prod_CNAME", col("min2"))
                .withColumn("Strength", col("min2"))
                .withColumn("DOI", col("MARKET"))
                .withColumn("DOIE", col("MARKET"))
                .select("ID", "Hosp_name", "Date",
                    "Prod_Name", "Prod_CNAME", "HOSP_ID", "Strength",
                    "DOI", "DOIE", "Units", "Sales")
        }

        DFArgs(panel)
    }

}