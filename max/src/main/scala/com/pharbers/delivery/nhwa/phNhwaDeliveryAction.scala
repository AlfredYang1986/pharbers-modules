package com.pharbers.delivery.nhwa

import java.util.{Date, UUID}

import com.pharbers.common.algorithm.{max_path_obj, phSparkCommonFuncTrait}
import com.pharbers.pactions.actionbase._
import org.apache.spark.sql.functions._

/**
  * Created by jeorch on 18-6-12.
  */
object phNhwaDeliveryAction {
    def apply(args: Map[String, String]): pActionTrait = new phNhwaDeliveryAction(args)
}

class phNhwaDeliveryAction(args: Map[String, String]) extends pActionTrait with phSparkCommonFuncTrait {
    override val name: String = "delivery_data_action"
    override val defaultArgs: pActionArgs = NULLArgs

    override def perform(pr: pActionArgs): pActionArgs = {

        val mkt = args("mkt")
        val ym_condition = args("ym_condition")
        val maxSearchDF = pr.asInstanceOf[MapArgs].get("read_result_action").asInstanceOf[DFArgs].get

        val df_match_nhwa = pr.asInstanceOf[MapArgs].get("product_match_file").asInstanceOf[DFArgs].get
            .select("药品名称_标准","商品名_标准","药品规格_标准","剂型_标准","生产企业_标准","min1_标准","Pack_ID","商品名+SKU","毫克数").distinct()
        val df_match_hospital = pr.asInstanceOf[MapArgs].get("hospital_match_file").asInstanceOf[DFArgs].get
        val df_match_acc = pr.asInstanceOf[MapArgs].get("acc_match_file").asInstanceOf[DFArgs].get
        val df_match_area = pr.asInstanceOf[MapArgs].get("area_match_file").asInstanceOf[DFArgs].get

        val deliveryDF = {
            val df_filter = maxSearchDF.filter("Product <> '多美康片剂15MG10上海罗氏制药有限公司'")

            val df_new1 = df_filter.join(df_match_hospital, df_filter("first(Panel_ID)") === df_match_hospital("Panel_ID"), "left").drop(df_match_hospital("City")).drop(df_filter("first(Panel_ID)"))

            val df_new2 = df_new1.join(df_match_nhwa, df_new1("Product") === df_match_nhwa("min1_标准"), "left")

            val df_result1 = df_new2.select("Date","City","Product","商品名+SKU","商品名_标准","生产企业_标准","Province","City Tier","药品名称_标准","药品规格_标准","剂型_标准","sum(f_units)","sum(f_sales)","毫克数").withColumnRenamed("sum(f_units)","sum(Units)").withColumnRenamed("sum(f_sales)","sum(Sales)")
            val df_result2 = df_result1.withColumn("销售毫克数",df_result1("毫克数").*(df_result1("sum(Units)"))).drop(df_result1("毫克数"))

            val df_result3 = df_result2.join(df_match_acc, df_result2("药品名称_标准") === df_match_acc("分子名"), "left").drop("药品名称_标准")

            val df_result4 = df_result3.join(df_match_area, df_result3("Province") === df_match_area("省份") && df_result3("ACC1/ACC2") === df_match_area("ACC1/ACC2"), "left").drop(df_match_area("省份")).drop(df_match_area("ACC1/ACC2"))

            val df_temp = df_result4.withColumnRenamed("分子名", "molecule").withColumnRenamed("区域", "area")
                .withColumnRenamed("商品名+SKU", "p_sku").withColumnRenamed("商品名_标准", "p_standard")
                .withColumnRenamed("生产企业_标准", "company_standard")
                .withColumnRenamed("City Tier", "City_Tier").withColumnRenamed("ACC1/ACC2", "ACC")
                .withColumnRenamed("药品规格_标准", "medicine_standard").withColumnRenamed("剂型_标准", "dosage_form")
                .withColumnRenamed("sum(Sales)", "Sales").withColumnRenamed("sum(Units)", "Units")
                .withColumnRenamed("销售毫克数", "SalesMG")

            val df_result5 = df_temp.filter("area <> 'null'")
                .selectExpr("Date","City","Product","p_sku","p_standard","company_standard","province","City_Tier","area","molecule","ACC","medicine_standard","dosage_form","Units","Sales","SalesMG","concat(molecule,area) as power")
                .union(
                    df_temp.filter("area is null")
                        .selectExpr("Date","City","Product","p_sku","p_standard","company_standard","province","City_Tier","area","molecule","ACC","medicine_standard","dosage_form","Units","Sales","SalesMG","concat(molecule,'NA') as power")
                )

            val df_result6 = df_result5.withColumn("year", df_result5("Date").substr(0, 4)).withColumn("month", df_result5("Date").substr(5, 6))

            val df_result8 = df_result6.withColumn("market_name", when(col("Product").isNotNull, "麻醉市场").otherwise("麻醉市场"))

            val df_final = df_result8.select(df_result8("Date").as("时间"), df_result8("City").as("城市")
                , df_result8("Product").as("min2"), df_result8("p_sku").as("商品名+SKU"),
                df_result8("p_standard").as("商品名_标准"), df_result8("company_standard").as("生产企业_标准"),
                df_result8("year").as("年"), df_result8("month").as("月"),
                df_result8("Province").as("省份"), df_result8("City_Tier").as("City.Tier"),
                df_result8("market_name").as("市场I"), df_result8("area").as("区域"),
                df_result8("molecule").as("分子名"), df_result8("ACC").as("ACC1/ACC2"),
                df_result8("power").as("power"), df_result8("medicine_standard").as("规格"),
                df_result8("dosage_form").as("剂型"), df_result8("Units").as("销售数量"),
                df_result8("Sales").as("销售金额"),df_result8("SalesMG").as("销售毫克数"))

            df_final
        }


        val maxSearchResultName = UUID.randomUUID().toString
        val exportDataPath = max_path_obj.p_exportPath + maxSearchResultName
        val destFileName = s"${new Date().getTime}-${mkt}-${ym_condition}.csv"
        val destPath = max_path_obj.p_exportPath + destFileName

        deliveryDF
            .coalesce(1).write
            .format("csv")
            .option("header", value = true)
            .option("delimiter", 31.toChar.toString)
            .save(exportDataPath)

        move2ExportFolder(getResultFileFullPath(exportDataPath), destPath)

        StringArgs(destFileName)
    }
}
