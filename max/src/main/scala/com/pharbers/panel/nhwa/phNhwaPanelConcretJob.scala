package com.pharbers.panel.nhwa

import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions._
import com.pharbers.spark.phSparkDriver
import com.pharbers.pactions.actionbase._
import org.apache.spark.sql.types.{DoubleType, LongType}

object phNhwaPanelConcretJob {
    def apply(args: MapArgs): pActionTrait = new phNhwaPanelConcretJob(args)
}

class phNhwaPanelConcretJob(override val defaultArgs : pActionArgs) extends pActionTrait {
    override val name: String = "panel"
    lazy val sparkDriver: phSparkDriver = phSparkDriver()

    override def perform(args : pActionArgs): pActionArgs = {

        val ym = defaultArgs.asInstanceOf[MapArgs].get("ym").asInstanceOf[StringArgs].get
        val mkt = defaultArgs.asInstanceOf[MapArgs].get("mkt").asInstanceOf[StringArgs].get

        val cpa = args.asInstanceOf[MapArgs].get("cpa").asInstanceOf[DFArgs].get
        val markets_match = args.asInstanceOf[MapArgs].get("markets_match_file").asInstanceOf[DFArgs].get
        val not_arrival_hosp_file = args.asInstanceOf[MapArgs].get("not_arrival_hosp_file").asInstanceOf[DFArgs].get
        val not_published_hosp_file = args.asInstanceOf[MapArgs].get("not_published_hosp_file").asInstanceOf[DFArgs].get
        val full_hosp_file : DataFrame = args.asInstanceOf[MapArgs].get("full_hosp_file").asInstanceOf[DFArgs].get
        val product_match_file = args.asInstanceOf[MapArgs].get("product_match_file").asInstanceOf[DFArgs].get
        val universe_file = args.asInstanceOf[MapArgs].get("universe_file").asInstanceOf[DFArgs].get

        def getPanelFile(ym: String, mkt: String) : pActionArgs = {
            val full_cpa = fullCPA(cpa, ym)
            val product_match = trimProductMatch(product_match_file)
            val universe = trimUniverse(universe_file, mkt)
            val markets_product_match = product_match.join(markets_match, markets_match("通用名_原始") === product_match("通用名"))
            val filted_panel = full_cpa.join(universe, full_cpa("HOSPITAL_CODE") === universe("ID"))
            val panelDF = trimPanel(filted_panel, markets_product_match)
            DFArgs(panelDF)
        }

        def fullCPA(cpa: DataFrame, ym: String): DataFrame = {
            val filter_month = ym.takeRight(2).toInt.toString
            val primal_cpa = cpa.filter(s"YM like '$ym'")
            val not_arrival_hosp = not_arrival_hosp_file
                .withColumnRenamed("月份", "month")
                .filter(s"month like '%$filter_month%'")
                .withColumnRenamed("医院编码", "ID")
                .select("ID")
            val not_published_hosp = not_published_hosp_file
                .withColumnRenamed("id", "ID")
            val miss_hosp = not_arrival_hosp.union(not_published_hosp).distinct()
            val reduced_cpa = primal_cpa.join(miss_hosp, primal_cpa("HOSPITAL_CODE") === miss_hosp("ID"), "left").filter("ID is null").drop("ID")
            val full_hosp_id = full_hosp_file.filter(s"MONTH like $filter_month")
            val full_hosp = miss_hosp.join(full_hosp_id, full_hosp_id("HOSPITAL_CODE") === miss_hosp("ID")).drop("ID").select(reduced_cpa.columns.head, reduced_cpa.columns.tail:_*)

            import sparkDriver.ss.implicits._
            reduced_cpa.union(full_hosp).withColumn("HOSPITAL_CODE", 'HOSPITAL_CODE.cast(LongType))
        }

        def trimProductMatch(product_match_file: DataFrame): DataFrame = {
            product_match_file
                .withColumnRenamed("药品名称", "NAME")
                .withColumnRenamed("商品名", "PRODUCT_NAME")
                .withColumnRenamed("剂型", "APP2_COD")
                .withColumnRenamed("规格", "PACK_DES")
                .withColumnRenamed("包装数量", "PACK_NUMBER")
                .withColumnRenamed("生产企业", "CORP_NAME")
                .withColumnRenamed("商品名_标准", "s_PRODUCT_NAME")
                .withColumnRenamed("药品规格_标准", "s_PACK_DES")
                .withColumnRenamed("剂型_标准", "s_APP2_COD")
                .withColumnRenamed("生产企业_标准", "s_CORP_NAME")
                .withColumn("min2",
                    when(col("min1_标准") =!= "", col("min1_标准"))
                            .otherwise(col("s_PRODUCT_NAME") + col("s_APP2_COD") + col("s_PACK_DES") + col("PACK_NUMBER") + col("PACK_NUMBER"))
                )
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

        getPanelFile(ym, mkt)
    }

}