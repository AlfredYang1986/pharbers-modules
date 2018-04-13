package com.pharbers.calc

import org.apache.spark.sql.functions._
import com.pharbers.spark.phSparkDriver
import com.pharbers.pactions.actionbase._

object phMaxCalcActionq {
    def apply(args: pActionArgs = NULLArgs): pActionTrait = new phMaxCalcActionq(args)
}

class phMaxCalcActionq(override val defaultArgs: pActionArgs) extends pActionTrait {
    override val name: String = "max_calc_action"

    override implicit def progressFunc(progress: Double, flag: String): Unit = {}

    override def perform(pr: pActionArgs)(implicit f: (Double, String) => Unit): pActionArgs = {
        val sparkDriver = phSparkDriver()
        import sparkDriver.ss.implicits._

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
                    .withColumnRenamed("西药收入", "WEST_MEDICINE_INCOME")
                    .withColumnRenamed("Factor", "FACTOR")
                    .withColumnRenamed("Province", "PROVINCE")
                    .selectExpr("PHA_ID", "IS_PANEL_HOSP", "NEED_MAX_HOSP", "SEGMENT", "WEST_MEDICINE_INCOME", "FACTOR", "PROVINCE", "Prefecture")
        }

        val panelSumed = {
            panelDF.groupBy("YM", "min1", "HOSP_ID")
                    .agg(Map("Units" -> "sum", "Sales" -> "sum"))
                    .withColumnRenamed("sum(Sales)", "sumSales")
                    .withColumnRenamed("sum(Units)", "sumUnits")
                    .withColumn("sumHYM", concat(col("HOSP_ID"), col("YM"), col("min1")))
                    .select("sumHYM", "sumSales", "sumUnits")
        }

        val joinData = {
            (panelDF.select("YM", "min1").distinct() join universeDF)
                    .withColumn("HYM", concat(col("PHA_ID"), col("YM"), col("min1")))
        }

        val fullData = {
            joinData.join(panelSumed, joinData("HYM") === panelSumed("sumHYM"), "left")
                    .withColumn("s_Sales", when($"sumSales".isNull, 0.0).otherwise($"sumSales"))
                    .withColumn("s_Units", when($"sumUnits".isNull, 0.0).otherwise($"sumUnits"))
                    .withColumn("SYM", concat(col("SEGMENT"), col("YM"), col("min1")))
                    .drop("HYM", "sumHYM")
        }

        val groupData = {
            fullData.filter(col("NEED_MAX_HOSP") === 1)
                    .groupBy("SYM")
                    .agg(Map("s_Sales" -> "sum", "s_Units" -> "sum", "WEST_MEDICINE_INCOME" -> "sum"))
                    .withColumnRenamed("sum(s_Sales)", "sumSales")
                    .withColumnRenamed("sum(s_Units)", "sumUnits")
                    .withColumnRenamed("sum(WEST_MEDICINE_INCOME)", "sumWestIncome")
                    .withColumn("aveSales", col("sumSales") / col("sumWestIncome"))
                    .withColumn("aveUnits", col("sumUnits") / col("sumWestIncome"))
                    .filter(col("aveSales") > 0 and col("aveUnits") > 0)
                    .select("SYM", "aveSales", "aveUnits")
        }

        val result = {
            fullData.join(groupData, fullData("SYM") === groupData("SYM"), "left").drop("SYM")
                    .withColumn("f_sales",
                        when($"IS_PANEL_HOSP" === 1, $"sumSales")
                                .when($"aveSales".isNotNull, $"aveSales" * $"WEST_MEDICINE_INCOME" * $"FACTOR")
                                .otherwise(0.0)
                    )
                    .withColumn("f_units",
                        when($"IS_PANEL_HOSP" === 1, $"sumUnits")
                                .when($"aveSales".isNotNull, $"aveUnits" * $"WEST_MEDICINE_INCOME" * $"FACTOR")
                                .otherwise(0.0)
                    )
                    .filter($"f_sales" =!= 0 and $"f_units" =!= 0)
                    .selectExpr("min1", "PHA_ID", "f_sales", "f_units", "IS_PANEL_HOSP")
        }

        DFArgs(result)
    }

}