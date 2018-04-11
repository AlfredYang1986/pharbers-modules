package com.pharbers.calc

import com.pharbers.pactions.actionbase._
import org.apache.spark.sql.functions.col

object phMaxCalcAction {
    def apply(args: pActionArgs = NULLArgs): pActionTrait = new phMaxCalcAction(args)
}

class phMaxCalcAction(override val defaultArgs: pActionArgs) extends pActionTrait {
    override val name: String = "max_calc_action"
    override implicit def progressFunc(progress: Double, flag: String) : Unit = {}

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

        val joinDataWithEmptyValue = (panelDF.select("YM", "min1") join universeDF)

        val joinData = joinDataWithEmptyValue.join(panelSumed, joinDataWithEmptyValue("PHA_ID") === panelSumed("sumHosp_ID")
            && joinDataWithEmptyValue("YM") === panelSumed("sumYM")
            && joinDataWithEmptyValue("min1") === panelSumed("sumMin1"), "left")

        val segmentDF = (joinData.filter(col("NEED_MAX_HOSP") === "1"))
            .groupBy("SEGMENT", "min1", "YM")
            .agg(Map("sumSales" -> "sum", "sumUnits" -> "sum", "westMedicineIncome" -> "sum"))
            .withColumnRenamed("SEGMENT", "s_SEGMENT")
            .withColumnRenamed("min1", "s_min1")
            .withColumnRenamed("YM", "s_YM")
            .withColumnRenamed("sum(sumSales)", "s_sumSales")
            .withColumnRenamed("sum(sumUnits)", "s_sumUnits")
            .withColumnRenamed("sum(westMedicineIncome)", "s_westMedicineIncome")

        val secondSum = joinData
//        val secondSum = (joinData.filter(col("NEED_MAX_HOSP") === "1"))
//            .groupBy("SEGMENT", "min1", "YM")
//            .agg(Map("sumSales" -> "sum", "sumUnits" -> "sum", "westMedicineIncome" -> "sum",
//                "PHA_ID" -> "first", "Factor" -> "first",
//                "IS_PANEL_HOSP" -> "first", "NEED_MAX_HOSP" -> "first",
//                "Prefecture" -> "first", "sumYM" -> "first",
//                "sumMin1" -> "first", "sumHosp_ID" -> "first"))
//            .withColumnRenamed("sum(sumSales)", "sumSales")
//            .withColumnRenamed("sum(sumUnits)", "sumUnits")
//            .withColumnRenamed("sum(westMedicineIncome)", "westMedicineIncome")
//            .withColumnRenamed("first(PHA_ID)", "PHA_ID")
//            .withColumnRenamed("first(Factor)", "Factor")
//            .withColumnRenamed("first(IS_PANEL_HOSP)", "IS_PANEL_HOSP")
//            .withColumnRenamed("first(NEED_MAX_HOSP)", "NEED_MAX_HOSP")
//            .withColumnRenamed("first(Prefecture)", "Prefecture")
//            .withColumnRenamed("first(sumYM)", "sumYM")
//            .withColumnRenamed("first(sumMin1)", "sumMin1")
//            .withColumnRenamed("first(sumHosp_ID)", "sumHosp_ID")
//            .union(joinData.filter(col("NEED_MAX_HOSP") =!= "1"))

        val thirdSum1 = (secondSum.filter(col("IS_PANEL_HOSP") === "1"))
            .withColumn("f_sales", secondSum("sumSales"))
            .withColumn("f_units", secondSum("sumUnits"))

        val thirdSum2_temp = secondSum.filter(col("IS_PANEL_HOSP") =!= "1")
            .join(segmentDF, secondSum("SEGMENT") === segmentDF("s_SEGMENT")
                && secondSum("min1") === segmentDF("s_min1")
                && secondSum("YM") === segmentDF("s_YM"))
            .drop("s_SEGMENT","s_min1","s_YM","sumSales","sumUnits","westMedicineIncome")
            .withColumnRenamed("s_sumSales","sumSales")
            .withColumnRenamed("s_sumUnits","sumUnits")
            .withColumnRenamed("s_westMedicineIncome","westMedicineIncome")

        val thirdSum2_1 = thirdSum2_temp.filter(col("sumUnits") < 0 && col("sumSales") < 0)
            .withColumn("f_sales", secondSum("sumSales")*0)
            .withColumn("f_units", secondSum("sumUnits")*0)

        val thirdSum2_2 = thirdSum2_temp.filter(!(col("sumUnits") < 0 && col("sumSales") < 0))
            .withColumn("f_sales", secondSum("sumSales")*secondSum("westMedicineIncome")*secondSum("Factor"))
            .withColumn("f_units", secondSum("sumUnits")*secondSum("westMedicineIncome")*secondSum("Factor"))

        val thirdSumFinal = thirdSum1.union(thirdSum2_1.union(thirdSum2_2))

        //        thirdSumFinal.show(10)
        println(thirdSumFinal.count())
        //        val test = thirdSumFinal.agg(Map("f_sales" -> "sum", "f_units" -> "sum"))
        //        test.show()

        NULLArgs
    }

}