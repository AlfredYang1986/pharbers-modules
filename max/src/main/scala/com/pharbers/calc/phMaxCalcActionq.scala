package com.pharbers.calc

import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.{DoubleType, LongType}

import com.pharbers.pactions.actionbase._
import com.pharbers.spark.phSparkDriver

object phMaxCalcActionq {
    def apply(args: pActionArgs = NULLArgs): pActionTrait = new phMaxCalcActionq(args)
}

class phMaxCalcActionq(override val defaultArgs: pActionArgs) extends pActionTrait {
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
                    .selectExpr("YM", "min1", "MARKET", "HOSP_ID", "Sales", "Units")
        }

        val universeDF = {
            pr.asInstanceOf[MapArgs].get("universe_data").asInstanceOf[DFArgs].get
                    .withColumnRenamed("PHA ID", "PHA_ID")
                    .withColumnRenamed("If Panel_All", "IS_PANEL_HOSP")
                    .withColumnRenamed("If Panel_To Use", "NEED_MAX_HOSP")
                    .withColumnRenamed("Segment", "SEGMENT")
                    .selectExpr("PHA_ID", "IS_PANEL_HOSP", "NEED_MAX_HOSP", "SEGMENT")
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

        val joinData = {
            (panelDF join universeDF.filter(col("NEED_MAX_HOSP") === "1"))
                    .drop("HOSP_ID", "Sales", "Units")
        }

        val calcData = {
            joinData.join(
                panelSumed,
                joinData("YM") <=> panelSumed("sumYM")
                        && joinData("min1") <=> panelSumed("sumMin1")
                        && joinData("PHA_ID") <=> panelSumed("sumHosp_ID"),
                "left"
            )
        }


        val a = panelDF.select("HOSP_ID").distinct().count()
        val b = joinData.select("PHA_ID").distinct().count()


        calcData.show(false)
        NULLArgs
    }

}