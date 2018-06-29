package com.pharbers.calc.actions

import com.pharbers.pactions.actionbase._
import com.pharbers.spark.phSparkDriver
import org.apache.spark.sql.functions.{col, when}

/**
  * Created by jeorch on 18-5-3.
  */
object phMaxCalcActionForCNS_R {
    def apply(args: pActionArgs = NULLArgs): pActionTrait = new phMaxCalcActionForCNS_R(args)
}

class phMaxCalcActionForCNS_R(override val defaultArgs: pActionArgs) extends pActionTrait {
    override val name: String = "max_calc_action"

    lazy val sparkDriver: phSparkDriver = phSparkDriver()
    import sparkDriver.ss.implicits._

    override def perform(pr: pActionArgs): pActionArgs = {

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
                .selectExpr("PHA_ID", "Factor1", "Factor2", "IS_PANEL_HOSP", "NEED_MAX_HOSP", "SEGMENT", "Province", "Prefecture", "westMedicineIncome")
        }

        val panelSummed = {
            panelDF.groupBy("YM", "min1", "HOSP_ID")
                .agg(Map("Units" -> "sum", "Sales" -> "sum"))
                .withColumnRenamed("YM", "sumYM")
                .withColumnRenamed("min1", "sumMin1")
                .withColumnRenamed("HOSP_ID", "sumHosp_ID")
                .withColumnRenamed("sum(Sales)", "sumSales")
                .withColumnRenamed("sum(Units)", "sumUnits")
        }

        val joinDataWithEmptyValue = panelDF.select("YM", "min1").distinct() join universeDF

        val joinData = {
            joinDataWithEmptyValue
                .join(panelSummed,
                    joinDataWithEmptyValue("PHA_ID") === panelSummed("sumHosp_ID")
                        && joinDataWithEmptyValue("YM") === panelSummed("sumYM")
                        && joinDataWithEmptyValue("min1") === panelSummed("sumMin1"),
                    "left")
                .withColumn("j_sumSales", when($"sumSales".isNull, 0.0).otherwise($"sumSales"))
                .withColumn("j_sumUnits", when($"sumUnits".isNull, 0.0).otherwise($"sumUnits"))
                .drop("sumSales", "sumUnits")
                .withColumnRenamed("j_sumSales", "sumSales")
                .withColumnRenamed("j_sumUnits", "sumUnits")
        }

        val segmentDF = {
            joinData.filter(col("NEED_MAX_HOSP") === "1")
                .groupBy("SEGMENT", "min1", "YM")
                .agg(Map("sumSales" -> "sum", "sumUnits" -> "sum", "westMedicineIncome" -> "sum"))
                .withColumnRenamed("SEGMENT", "s_SEGMENT")
                .withColumnRenamed("min1", "s_min1")
                .withColumnRenamed("YM", "s_YM")
                .withColumnRenamed("sum(sumSales)", "s_sumSales")
                .withColumnRenamed("sum(sumUnits)", "s_sumUnits")
                .withColumnRenamed("sum(westMedicineIncome)", "s_westMedicineIncome")
                .withColumn("avg_Sales", $"s_sumSales" / $"s_westMedicineIncome")
                .withColumn("avg_Units", $"s_sumUnits" / $"s_westMedicineIncome")
                .drop("s_sumSales", "s_sumUnits", "s_westMedicineIncome")
        }

        val resultDF = {
            joinData
                .join(segmentDF,
                    joinData("SEGMENT") === segmentDF("s_SEGMENT")
                        && joinData("min1") === segmentDF("s_min1")
                        && joinData("YM") === segmentDF("s_YM"))
                .drop("s_SEGMENT", "s_min1", "s_YM")
                .withColumn("Factor", when($"min1" like "%粉针剂%",$"Factor1")
                    .otherwise(when($"min1" like "%注射剂%",$"Factor1")
                        .otherwise($"Factor2")))
                .withColumn("f_sales",
                    when($"IS_PANEL_HOSP" === 1, $"sumSales").otherwise(
                        when($"avg_Sales" < 0.0 or $"avg_Units" < 0.0, 0.0)
                            .otherwise($"Factor" * $"avg_Sales" * $"westMedicineIncome")
                    ))
                .withColumn("f_units",
                    when($"IS_PANEL_HOSP" === 1, $"sumUnits").otherwise(
                        when($"avg_Sales" < 0.0 or $"avg_Units" < 0.0, 0.0)
                            .otherwise($"Factor" * $"avg_Units" * $"westMedicineIncome")
                    ))
                .drop("s_sumSales", "s_sumUnits", "s_westMedicineIncome")
                .filter(col("f_units") =!= 0 || col("f_sales") =!= 0)
                .withColumnRenamed("PHA_ID", "Panel_ID")
                .withColumnRenamed("YM", "Date")
                .withColumnRenamed("Prefecture", "City")
                .withColumnRenamed("min1", "Product")
                .select("Date", "Province", "City", "Panel_ID", "Product", "Factor", "f_sales", "f_units", "MARKET")
        }

        DFArgs(resultDF)
    }

}
