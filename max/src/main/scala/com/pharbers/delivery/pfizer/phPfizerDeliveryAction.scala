package com.pharbers.delivery.pfizer

import java.util.{Date, UUID}

import com.pharbers.common.algorithm.{max_path_obj, phSparkCommonFuncTrait}
import com.pharbers.pactions.actionbase._
import org.apache.spark.sql.Column
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._

/**
  * Created by jeorch on 18-6-12.
  */
object phPfizerDeliveryAction {
    def apply(args: Map[String, String]): pActionTrait = new phPfizerDeliveryAction(args)
}

class phPfizerDeliveryAction(args: Map[String, String]) extends pActionTrait with phSparkCommonFuncTrait {
    override val name: String = "delivery_data_action"
    override val defaultArgs: pActionArgs = NULLArgs

    override def perform(pr: pActionArgs): pActionArgs = {

        val mkt = args("mkt")
        val ym_condition = args("ym_condition")
        val maxSearchDF = pr.asInstanceOf[MapArgs].get("read_result_action").asInstanceOf[DFArgs].get
            .withColumnRenamed("sum(f_units)", "f_units")
            .withColumnRenamed("sum(f_sales)", "f_sales")
            .withColumnRenamed("first(Panel_ID)", "Panel_ID")

        val df_universe = pr.asInstanceOf[MapArgs].get("universe_file").asInstanceOf[DFArgs].get
        val df_bridge = pr.asInstanceOf[MapArgs].get("bridge_file").asInstanceOf[DFArgs].get
        val df_city_match = pr.asInstanceOf[MapArgs].get("city_match_file").asInstanceOf[DFArgs].get

        val deliveryDF = {

            //Deal universe.
            val universe_condition = df_universe.filter(col("BED_NUM").cast(IntegerType).gt(99)).select("PHA_ID")
            val hospital_city = df_universe.select("PHA_ID", "CITY")

            //Deal pfc_file.
            //TODO: find another better function
            val pfc_format = df_bridge.select("Proc_Name", "pfc", "pack_no").distinct()
                .withColumn("pfc", when(col("pfc").lt(1E4), concat(col("pfc").*(0), col("pfc").*(0), col("pfc").*(0), col("pfc")))
                    .otherwise(when(col("pfc").lt(1E5), concat(col("pfc").*(0), col("pfc").*(0), col("pfc")))
                        .otherwise(when(col("pfc").lt(1E6), concat(col("pfc").*(0), col("pfc")))
                            .otherwise(col("pfc")))))

            //Deal city_file.
            val city_geo = df_city_match.select("GEO_CD", "City")

            //Start match generate delivery.
            val max_matched = maxSearchDF
                .join(hospital_city, col("Panel_ID") === hospital_city("PHA_ID"), "left").drop("City").drop("PHA_ID")
                .join(city_geo, col("CITY") === city_geo("City"), "left")
                .join(pfc_format, col("Product") === pfc_format("Proc_Name"), "left")

            val max_dvp = max_matched.filter(col("MARKET") === "DVP")
            val max_other = max_matched.filter(col("MARKET") =!= "DVP")
                .join(universe_condition, col("Panel_ID") === universe_condition("PHA_ID")).drop("PHA_ID")

            val max_all = max_dvp union max_other

            val max_renamed = max_all
                .withColumnRenamed("Date", "Period_Code")
                .withColumnRenamed("GEO_CD","Geography_id")
                .withColumnRenamed("pfc","Pack_ID")
                .withColumn("unit", col("f_units")/col("pack_no"))

            val max_grouped = max_renamed
                .groupBy("Period_Code", "Geography_id", "Pack_ID")
                .agg(Map("f_sales" -> "sum", "f_units" -> "sum", "unit" -> "sum"))
                .withColumnRenamed("sum(f_sales)", "LC")
                .withColumnRenamed("sum(f_units)", "SU")
                .withColumnRenamed("sum(unit)", "UN")

            val max_filtered = max_grouped.filter(col("Pack_ID").isNotNull)

            val max_add_col = max_filtered
                .withColumn("Channel_id", when(col("Pack_ID").isNotNull, "P00")
                    .otherwise("P00"))
                .withColumn("temp_M", when(col("Pack_ID").isNotNull, "M")
                    .otherwise("M"))
                .withColumn("LCD", when(col("Pack_ID").isNotNull, 0)
                    .otherwise(0))
                .withColumn("CU", when(col("Pack_ID").isNotNull, 0)
                    .otherwise(0))
                .withColumn("Period_Code", when(col("Pack_ID").isNotNull, concat(col("Period_Code").substr(0, 4), col("temp_M"), col("Period_Code").substr(5, 2)))
                    .otherwise(concat(col("Period_Code").substr(0, 4), col("temp_M"), col("Period_Code").substr(5, 2))))

            val max_final = max_add_col.select("Geography_id", "Channel_id", "Pack_ID", "Period_Code", "UN", "CU", "SU", "LC", "LCD")
                .withColumn("LC", round(col("LC"), 1))
                .withColumn("SU", col("SU").cast(IntegerType))
                .withColumn("UN", col("UN").cast(IntegerType))

            max_final
        }


        val maxSearchResultName = UUID.randomUUID().toString
        val exportDataPath = max_path_obj.p_exportPath + maxSearchResultName
        val destFileName = s"${new Date().getTime}-${mkt}-${ym_condition}.txt"
        val destPath = max_path_obj.p_exportPath + destFileName

        deliveryDF
            .coalesce(1).write
            .format("csv")
            .option("header", value = true)
            .option("delimiter", 124.toChar.toString)
            .save(exportDataPath)

        move2ExportFolder(getResultFileFullPath(exportDataPath), destPath)

        StringArgs(destFileName)
    }

}
