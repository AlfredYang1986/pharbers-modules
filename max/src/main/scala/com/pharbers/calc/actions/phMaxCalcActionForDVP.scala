package com.pharbers.calc.actions

import com.pharbers.pactions.actionbase._
import com.pharbers.spark.phSparkDriver
import org.apache.spark.sql.functions.{col, when}

/**
  * Created by jeorch on 18-5-3.
  */
object phMaxCalcActionForDVP{
    def apply(args: pActionArgs = NULLArgs): pActionTrait = new phMaxCalcActionForDVP(args)
}

class phMaxCalcActionForDVP(override val defaultArgs: pActionArgs) extends pActionTrait {
    override val name: String = "max_calc_action"

    lazy val sparkDriver: phSparkDriver = phSparkDriver()
    import sparkDriver.ss.implicits._

    override def perform(pr: pActionArgs): pActionArgs = {

        val panelDF = {
            pr.asInstanceOf[MapArgs].get("panel_data").asInstanceOf[DFArgs].get
                .selectExpr("Date", "Prod_Name", "HOSP_ID", "Sales", "Units")
        }

        val universeDF = {
            pr.asInstanceOf[MapArgs].get("universe_data").asInstanceOf[DFArgs].get
                .withColumnRenamed("Prefecture", "City")
                .withColumnRenamed("PHA ID", "HOSP_ID")
                .selectExpr("Province", "City", "HOSP_ID")
        }

        val coefDF = {
            pr.asInstanceOf[MapArgs].get("coef_data").asInstanceOf[DFArgs].get
                .withColumnRenamed("City", "City2")
        }

        val panelMergeUniverse = {
            panelDF.join(universeDF, panelDF("HOSP_ID") === universeDF("HOSP_ID"))
                .drop(universeDF("HOSP_ID"))
                .withColumn("City2",when(col("City") === "福州市"||col("City") === "厦门市"||col("City") === "泉州市", "福厦泉市")
                    .otherwise(when(col("City") === "珠海市"||col("City") === "东莞市"||col("City") === "中山市"||col("City") === "佛山市", "珠三角市")
                        .otherwise(col("City"))))
        }

        val panelMergeUniverseMergeCoef = {
            panelMergeUniverse.join(coefDF, panelMergeUniverse("City2")===coefDF("City2"), "left")
                .withColumn("coef", when($"coef".isNull, 1.204832714).otherwise($"coef"))
                .withColumn("Sales", when($"Sales" === 0 or $"Units" === 0, 0).otherwise($"Sales"))
                .withColumn("Units", when($"Sales" === 0 or $"Units" === 0, 0).otherwise($"Units"))
                .withColumn("f_sales", $"Sales"*$"coef")
                .withColumn("f_units", $"Units"*$"coef")
        }

        val max_result = {
            panelMergeUniverseMergeCoef.groupBy("Province", "City","Date","Prod_Name","coef")
                .agg(Map("Sales" -> "sum", "Units" -> "sum", "f_sales" -> "sum", "f_units" -> "sum"))
                .withColumnRenamed("sum(Sales)", "Sales")
                .withColumnRenamed("sum(Units)", "Units")
                .withColumnRenamed("sum(f_sales)", "f_sales")
                .withColumnRenamed("sum(f_units)", "f_units")
                .withColumnRenamed("Prod_Name", "Product")
                .select("Province", "City", "Date", "Product", "coef", "Sales", "Units", "f_sales", "f_units")
        }

        DFArgs(max_result)
    }

}