package com.pharbers.panel.pfizer.actions

import com.pharbers.pactions.actionbase._
import com.pharbers.spark.phSparkDriver
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions.{col, when, _}
import org.apache.spark.sql.types.{DoubleType, LongType}

/**
  * Created by jeorch on 18-4-18.
  */
object phPfizerPanelCommonAction {
    def apply(args: MapArgs): pActionTrait = new phPfizerPanelCommonAction(args)
}

class phPfizerPanelCommonAction(override val defaultArgs : pActionArgs) extends pActionTrait {
    override val name: String = "panel"
    lazy val sparkDriver: phSparkDriver = phSparkDriver()

    override def perform(args : pActionArgs): pActionArgs = {

        val ym = defaultArgs.asInstanceOf[MapArgs].get("ym").asInstanceOf[StringArgs].get
        val mkt = defaultArgs.asInstanceOf[MapArgs].get("mkt").asInstanceOf[StringArgs].get

        val cpa = args.asInstanceOf[MapArgs].get("cpa").asInstanceOf[DFArgs].get
        val gyc = args.asInstanceOf[MapArgs].get("gyc").asInstanceOf[DFArgs].get

        val universe_file = args.asInstanceOf[MapArgs].get("universe_file").asInstanceOf[DFArgs].get

        //通用名市场定义 =>表b0
        val markets_match = args.asInstanceOf[MapArgs].get("markets_match_file").asInstanceOf[DFArgs].get
            .filter(s"Market like '${mkt}'")

        //待匹配min1_标准的min1表
        val splitMktResultDF = args.asInstanceOf[MapArgs].get("SplitMarketAction").asInstanceOf[DFArgs].get
        val not_arrival_hosp_file = args.asInstanceOf[MapArgs].get("not_arrival_hosp_file").asInstanceOf[DFArgs].get    //1-xx月未到医院名单
        val full_hosp_file : DataFrame = args.asInstanceOf[MapArgs].get("full_hosp_file").asInstanceOf[DFArgs].get  //补充医院
            .withColumn("MONTH", when(col("MONTH").>=(10), col("MONTH"))
                .otherwise(concat(col("MONTH").*(0).cast("int"), col("MONTH"))))
            .withColumn("YM", concat(col("YEAR"), col("MONTH")))
            .withColumn("min1", concat(col("PRODUCT_NAME"),col("APP2_COD"),col("PACK_DES"),col("PACK_NUMBER"),col("CORP_NAME")))

        def getPanelFile: pActionArgs = {

            val full_cpa_gyc = fullCPAandGYCX
            val filter_cpa_gyc = full_cpa_gyc.join(markets_match, full_cpa_gyc("MOLE_NAME") === markets_match("通用名_原始"))
            val universe = trimUniverse
            val filted_panel = filter_cpa_gyc.join(universe, filter_cpa_gyc("HOSPITAL_CODE") === universe("ID"))
            val panelDF = trimPanel(filted_panel, splitMktResultDF)
            DFArgs(panelDF)
        }

        def fullCPAandGYCX = {

            val filter_month = ym.takeRight(2).toInt
            val primal_cpa = cpa.filter(s"YM like '$ym'")
            val not_arrival_hosp = not_arrival_hosp_file
                .withColumnRenamed("月份", "month")
                .filter(s"month like '%$filter_month%'")
                .withColumnRenamed("医院编码", "ID")
                .select("ID")
            val miss_hosp = not_arrival_hosp.distinct()
            val reduced_cpa = primal_cpa.join(miss_hosp, primal_cpa("HOSPITAL_CODE") === miss_hosp("ID"), "left").filter("ID is null").drop("ID")
            val full_hosp_id = full_hosp_file.filter(col("MONTH") === filter_month)
            val full_hosp = miss_hosp.join(full_hosp_id, miss_hosp("ID") === full_hosp_id("HOSPITAL_CODE")).drop("ID").select(reduced_cpa.columns.head, reduced_cpa.columns.tail:_*)

            import sparkDriver.ss.implicits._
            reduced_cpa.union(full_hosp).union(gyc.filter(s"YM like '$ym'").select(reduced_cpa.columns.head, reduced_cpa.columns.tail:_*)).withColumn("HOSPITAL_CODE", 'HOSPITAL_CODE.cast(LongType))
        }

        def trimUniverse = {
            import sparkDriver.ss.implicits._
            universe_file.withColumnRenamed("样本医院编码", "ID")
                .withColumnRenamed("PHA医院名称", "HOSP_NAME")
                .withColumnRenamed("PHA ID", "HOSP_ID")
                .withColumnRenamed("市场", "DOI")
                .withColumnRenamed("If Panel_All", "SAMPLE")
                .filter("SAMPLE like '1'")
                .selectExpr("ID", "HOSP_NAME", "HOSP_ID", "DOI", "DOI as DOIE")
                .withColumn("ID", 'ID.cast(LongType))
        }

        def trimPanel(filted_panel: DataFrame, min1_match: DataFrame): DataFrame = {
            import sparkDriver.ss.implicits._
            val temp = filted_panel.join(min1_match, filted_panel("min1") === min1_match("min1")).drop(min1_match("min1"))
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

        getPanelFile
    }

}
