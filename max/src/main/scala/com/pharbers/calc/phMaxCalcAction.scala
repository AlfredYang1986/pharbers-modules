package com.pharbers.calc

import com.pharbers.pactions.actionbase._
import com.pharbers.spark.phSparkDriver
import org.apache.spark.sql.functions._
import org.apache.spark.sql.functions.col

object phMaxCalcAction {
    def apply(args: pActionArgs = NULLArgs): pActionTrait = new phMaxCalcAction(args)
}

class phMaxCalcAction(override val defaultArgs: pActionArgs) extends pActionTrait {
    override val name: String = "max_calc_action"
    override implicit def progressFunc(progress: Double, flag: String) : Unit = {}

    lazy val sparkDriver: phSparkDriver = phSparkDriver()
    import sparkDriver.ss.implicits._

    override def perform(pr: pActionArgs)(implicit f: (Double, String) => Unit) : pActionArgs = {

        val panelDF = {
            pr.asInstanceOf[MapArgs].get("panel_data").asInstanceOf[DFArgs].get
                .withColumnRenamed("Date", "YM")
                .withColumnRenamed("Strength", "min1")
                .withColumnRenamed("DOI", "MARKET")
                .selectExpr("YM", "min1", "HOSP_ID", "Sales", "Units")
        }

        val universeDF = {
            pr.asInstanceOf[MapArgs].get("universe_data").asInstanceOf[DFArgs].get
                .withColumnRenamed("PHA ID", "PHA_ID")
                .withColumnRenamed("If Panel_All", "IS_PANEL_HOSP")
                .withColumnRenamed("If Panel_To Use", "NEED_MAX_HOSP")
                .withColumnRenamed("Segment", "SEGMENT")
                .withColumnRenamed("西药收入", "westMedicineIncome")
                .selectExpr("PHA_ID", "Factor", "IS_PANEL_HOSP", "NEED_MAX_HOSP", "SEGMENT", "Prefecture", "westMedicineIncome")
        }

        val panelSumed = {
            panelDF.groupBy("YM", "min1", "HOSP_ID")
                .agg(Map("Units" -> "sum", "Sales" -> "sum"))
                .withColumnRenamed("YM", "sumYM")
                .withColumnRenamed("min1", "sumMin1")
                .withColumnRenamed("HOSP_ID", "sumHosp_ID")
                .withColumnRenamed("sum(Sales)", "sumSales")
                .withColumnRenamed("sum(Units)", "sumUnits")
        }

        val joinDataWithEmptyValue = (panelDF.select("YM", "min1").distinct() join universeDF)

        val joinData = joinDataWithEmptyValue.join(panelSumed, joinDataWithEmptyValue("PHA_ID") === panelSumed("sumHosp_ID")
            && joinDataWithEmptyValue("YM") === panelSumed("sumYM")
            && joinDataWithEmptyValue("min1") === panelSumed("sumMin1"), "left")
            .withColumn("j_sumSales", when($"sumSales".isNull, 0.0).otherwise($"sumSales"))
            .withColumn("j_sumUnits", when($"sumUnits".isNull, 0.0).otherwise($"sumUnits"))
            .drop("sumSales","sumUnits")
            .withColumnRenamed("j_sumSales","sumSales")
            .withColumnRenamed("j_sumUnits","sumUnits")

        val segmentDF = (joinData.filter(col("NEED_MAX_HOSP") === "1"))
            .groupBy("SEGMENT", "min1", "YM")
            .agg(Map("sumSales" -> "sum", "sumUnits" -> "sum", "westMedicineIncome" -> "sum"))
            .withColumnRenamed("SEGMENT", "s_SEGMENT")
            .withColumnRenamed("min1", "s_min1")
            .withColumnRenamed("YM", "s_YM")
            .withColumnRenamed("sum(sumSales)", "s_sumSales")
            .withColumnRenamed("sum(sumUnits)", "s_sumUnits")
            .withColumnRenamed("sum(westMedicineIncome)", "s_westMedicineIncome")
            .withColumn("avg_Sales",$"s_sumSales"/$"s_westMedicineIncome")
            .withColumn("avg_Units",$"s_sumUnits"/$"s_westMedicineIncome")

        val thirdSum2 = joinData
            .join(segmentDF, joinData("SEGMENT") === segmentDF("s_SEGMENT")
                && joinData("min1") === segmentDF("s_min1")
                && joinData("YM") === segmentDF("s_YM"))
            .drop("s_SEGMENT","s_min1","s_YM")
            .withColumn("t_sumSales", when($"IS_PANEL_HOSP" === 1,$"sumSales").otherwise(
                when($"avg_Sales".<(0.0),0.0)
                    .otherwise($"Factor"*$"avg_Sales"*$"westMedicineIncome")
            ))
            .withColumn("t_sumUnits", when($"IS_PANEL_HOSP" === 1,$"sumUnits").otherwise(
                when($"avg_Units".<(0.0),0.0)
                    .otherwise($"Factor"*$"avg_Units"*$"westMedicineIncome")
            ))
            .drop("s_sumSales","s_sumUnits","s_westMedicineIncome")
            .withColumn("f_sales",when($"IS_PANEL_HOSP" === 1 and $"sumSales" === 0.0, 0.0).otherwise($"t_sumSales"))
            .withColumn("f_units",when($"IS_PANEL_HOSP" === 1 and $"sumUnits" === 0.0, 0.0).otherwise($"t_sumUnits"))
            .drop("t_sumSales","t_sumUnits")

        val FinalSum = thirdSum2
            .filter(col("f_units") =!= 0 && col("f_sales") =!= 0)

        FinalSum.show(10)
//        println(FinalSum.count())
//        val test = FinalSum.agg(Map("f_sales" -> "sum", "f_units" -> "sum"))
//        test.show()

        NULLArgs
    }

}