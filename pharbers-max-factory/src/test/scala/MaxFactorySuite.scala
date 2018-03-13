import com.pharbers.spark.driver.phSparkDriver
import org.apache.spark.sql.SaveMode
import org.scalatest.FunSuite
/**
  * Created by jeorch on 18-3-6.
  */
class MaxFactorySuite extends FunSuite {

    val driver =  phSparkDriver()

//    test ("Test csv2RDD") {
//        val file_path = "/home/jeorch/work/max/files/NHWA/test2.csv"
//        val output_path = "/home/jeorch/work/max/files/NHWA/new.csv"
//        val df = driver.csv2RDD(file_path)
//        println("**********Fuck************")
//        df.select("City").show()
//        println("乱码?")
//
////        df.select("Panel_ID", "Date", new String(df("City").toString().getBytes("UTF-8"), 0, df("City").toString().length, "UTF-8")).show()
////        df.select("Panel_ID", "Date", "City").rdd.saveAsTextFile(output_path)
////        df.select("Panel_ID", "Date", "City").write.format("csv").save(output_path)
//        driver.ss.stop()
//    }

//    test("Test toDS") {
//        val spark = SparkSession.builder.appName("spark-test-max-factory").getOrCreate()
//        import spark.implicits._
//
//        case class HospitalArea(panel_id: String, province: String, city: String)
//
//        val caseClassDS = Seq(HospitalArea("PHA0000036", "新疆", "阿克苏地区")).toDS()
////        val caseClassDS = Seq(1, 2, 3).toDS()
//
//        caseClassDS.show()
//
//    }

    test("Test Max join") {
//        val max_result_file = "/home/jeorch/work/max/files/NHWA/max/max 結果.csv"
        val max_result_file = "/home/jeorch/work/max/files/NHWA/max/max_result.csv"
        val hospital_match_file = "/home/jeorch/work/max/files/NHWA/max/交付格式医院匹配表1.csv"
        val nhwa_match_file = "/home/jeorch/work/max/files/NHWA/max/nhwa匹配表.csv"
        val acc_match_file = "/home/jeorch/work/max/files/NHWA/max/match_acc.csv"
        val area_match_file = "/home/jeorch/work/max/files/NHWA/max/match_area.csv"
        val market_match_file = "/home/jeorch/work/max/files/NHWA/max/match_market.csv"

        val output_path = "/home/jeorch/work/max/files/NHWA/spark-result"

        val df_max = driver.csv2RDD(max_result_file)
//        println(s"******************df_max = ${df_max.count()}")
//            df_max.show()

            val gb_test = df_max.select(df_max("Panel_ID"),df_max("Date"),df_max("City"),df_max("Product"),df_max("Sales").cast("double"),df_max("Units").cast("double")).groupBy("Date","City","Product")
            val df_gb_sum = gb_test.agg(("Units","sum"),("Sales","sum"),("Panel_ID","first"))
//            df_gb_sum.show()
//            println(s"******************df_gb_sum = ${df_gb_sum.count()}")

            val df_filter = df_gb_sum.filter("Product <> '多美康片剂15MG10上海罗氏制药有限公司'")
//            println(s"******************df_filter = ${df_filter.filter("Product is null").count()}")

        val df_match_hospital = driver.csv2RDD(hospital_match_file)
//        println(s"******************df_match_hospital = ${df_match_hospital.count()}")

        val df_match_nhwa = driver.csv2RDD(nhwa_match_file).select("药品名称_标准","商品名_标准","药品规格_标准","剂型_标准","生产企业_标准","min1_标准","Pack_ID","商品名+SKU","毫克数").distinct()
//            println(s"******************df_match_nhwa = ${df_match_nhwa.count()}")
        val df_match_acc = driver.csv2RDD(acc_match_file)
        val df_match_area = driver.csv2RDD(area_match_file)
        val df_match_market = driver.csv2RDD(market_match_file)

        val df_new1 = df_filter.join(df_match_hospital, df_filter("first(Panel_ID)") === df_match_hospital("Panel_ID"), "left").drop(df_match_hospital("City")).drop(df_filter("first(Panel_ID)"))
//        df_new1.show()
//        println(s"******************df_new1 = ${df_new1.filter("Product is null").count()}")

        val df_new2 = df_new1.join(df_match_nhwa, df_new1("Product") === df_match_nhwa("min1_标准"), "left")
//        df_new2.show()
//        println(s"******************df_new2 = ${df_new2.filter("Product is null").count()}")

        val df_result1 = df_new2.select("Panel_ID","Date","City","Product","商品名+SKU","商品名_标准","生产企业_标准","省份","City Tier","药品名称_标准","药品规格_标准","剂型_标准","sum(Units)","sum(Sales)","毫克数")
//        df_result1.show()
//        println(s"******************df_result1 = ${df_result1.filter("Product is null").count()}")

        val df_result2 = df_result1.withColumn("销售毫克数",df_result1("毫克数").*(df_result1("sum(Units)"))).drop(df_result1("毫克数"))
//        df_result2.show()
//        println(s"******************df_result2 = ${df_result2.filter("Product is null").count()}")

        val df_result3 = df_result2.join(df_match_acc, df_result2("药品名称_标准") === df_match_acc("分子名"), "left").drop("药品名称")
//        df_result3.show()
//        println(s"******************df_result3 = ${df_result3.filter("Product is null").count()}")

        val df_result4 = df_result3.join(df_match_area, df_result3("省份") === df_match_area("省份") && df_result3("ACC1/ACC2") === df_match_area("ACC1/ACC2"), "left").drop(df_match_area("省份")).drop(df_match_area("ACC1/ACC2"))
//        df_result4.show()
//        println(s"******************df_result4 = ${df_result4.filter("Product is null").count()}")
//        println(s"******************df_result4 = ${df_result4.select("Panel_ID","Product").distinct().count()}")

//        val df_temp = df_result4.withColumnRenamed("分子名", "molecule").withColumnRenamed("区域", "area").withColumnRenamed("Product", "Product_temp").withColumnRenamed("Panel_ID", "Panel_ID_temp")
        val df_temp1 = df_result4.withColumnRenamed("分子名", "molecule").withColumnRenamed("区域", "area")
                .withColumnRenamed("商品名+SKU", "p_sku").withColumnRenamed("商品名_标准", "p_standard")
                .withColumnRenamed("生产企业_标准", "company_standard").withColumnRenamed("省份", "province")
                .withColumnRenamed("City Tier", "City_Tier").withColumnRenamed("ACC1/ACC2", "ACC")
                .withColumnRenamed("药品规格_标准", "medicine_standard").withColumnRenamed("剂型_标准", "dosage_form")
                .withColumnRenamed("sum(Sales)", "Sales").withColumnRenamed("sum(Units)", "Units")
                .withColumnRenamed("销售毫克数", "SalesMG")
//        println(s"******************df_temp = ${df_temp.filter("area <> 'null'").count()}")

//        val df_power = df_temp.filter("area <> 'null'").selectExpr("Panel_ID_temp", "Product_temp", "concat(molecule,area) as power").union(df_temp.filter("area is null").selectExpr("Panel_ID_temp", "Product_temp", "concat(molecule,'NA') as power"))
//        val df_power = df_temp.selectExpr("Panel_ID", "Product_temp", "concat(molecule,area) as power")
//            df_power.show(3000)
//            println(s"******************df_power = ${df_power.filter("power <> 'null'").count()}")
//            println(s"******************df_power = ${df_power.filter("Product_temp is null").count()}")

        val df_result5 = df_temp1.filter("area <> 'null'")
            .selectExpr("Date","City","Product","p_sku","p_standard","company_standard","province","City_Tier","area","molecule","ACC","medicine_standard","dosage_form","Units","Sales","SalesMG","concat(molecule,area) as power")
                .union(df_temp1.filter("area is null")
                    .selectExpr("Date","City","Product","p_sku","p_standard","company_standard","province","City_Tier","area","molecule","ACC","medicine_standard","dosage_form","Units","Sales","SalesMG","concat(molecule,'NA') as power")
                )

            //        val df_result5 = df_result4.join(df_power, df_result4("Panel_ID") === df_power("Panel_ID_temp") && df_result4("Product") === df_power("Product_temp"), "left")/*.drop(df_power("Product_temp")).drop(df_power("Panel_ID_temp"))*/
//        df_result5.show(3000)
//        println(s"******************df_result5.count = ${df_result5.count()}")
//        println(s"******************df_result5.product = ${df_result5.filter("Product <> 'null'").count()}")
//        println(s"******************df_result5.power = ${df_result5.filter("power <> 'null'").count()}")

        val df_result6 = df_result5.withColumn("year", df_result5("Date").substr(0, 4)).withColumn("month", df_result5("Date").substr(5, 6))
            df_result6.show()

        val df_result7 = df_result6.withColumn("market_num",df_result6("City_Tier").*(0).+(1).cast("int"))

        val df_result8 = df_result7.join(df_match_market, df_result7("market_num") === df_match_market("market_num"), "left").drop("market_num").select("Date","City","Product","p_sku","p_standard","company_standard","year","month","province","City_Tier","market_name","area","molecule","ACC","power","medicine_standard","dosage_form","Units","Sales","SalesMG")
//        df_result8.show()
//        println(s"******************df_result8 = ${df_result8.filter("Product is null").count()}")
//        println(s"******************df_result8 = ${df_result8.filter("power <> 'null'").count()}")
//
////        val df_fianl = df_result8.select(df_result8("Date").as("时间"),df_result8("City").as("城市")
////                ,df_result8("").as(""),df_result8("").as(""),
////                df_result8("").as(""),df_result8("").as(""),df_result8("").as(""),df_result8("").as(""),
////                df_result8("").as(""),df_result8("").as(""),df_result8("").as(""),df_result8("").as(""),
////                df_result8("").as(""),df_result8("").as(""),df_result8("").as(""),df_result8("").as(""),
////                df_result8("").as(""),df_result8("").as(""),df_result8("").as(""),df_result8("").as(""))
//
        val saveOptions = Map("header" -> "true", "path" -> s"${output_path}")
        df_result8.coalesce(1).write.format("csv").mode(SaveMode.Overwrite).options(saveOptions).save()
        driver.ss.stop()
        //val model = "时间,城市,min2,商品名+SKU,商品名_标准,生产企业_标准,年,月,省份,City.Tier,市场I,区域,分子名,ACC1/ACC2,power,规格,剂型,销售数量,销售金额,销售毫克数"
            //min2 = Product
            //分子名 = 药品名称

            //解决
            //市场I,年,月,区域,ACC1/ACC2,销售毫克数 = Units * 毫克数
            //power = 分子名 + 区域
    }

//    test ("Test SparkContext") {
//        val file_path = "/home/jeorch/work/max/files/NHWA/test.csv"
////        val file_path = "/home/jeorch/work/max/files/NHWA/max_result.csv"
//
//        val myRDD = driver.sc.textFile(file_path)
//
//        println(s"**********${}")
//
//        myRDD.collect().foreach(x => println(s"===${x}"))
//
//        driver.sc.stop()
//
//    }

//    test ("Test sqc load/save") {
//        val file_path = "/home/jeorch/work/max/files/NHWA/max 結果.csv"
//        val t = driver.sqc.load(file_path)
//
//        t.select("Panel_ID", "Date", "City")
//    }

}
