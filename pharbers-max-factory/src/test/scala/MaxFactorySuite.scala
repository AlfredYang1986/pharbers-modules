import com.pharbers.nhwa.DriverNHWA
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

//    test("Test Max join") {
////        val max_result_file = "/home/jeorch/work/max/files/NHWA/max/max 結果.csv"
//        val max_result_file = "/home/jeorch/work/max/files/NHWA/max/max_result.csv"
//        val hospital_match_file = "/home/jeorch/work/max/files/NHWA/max/交付格式医院匹配表.csv"
//        val nhwa_match_file = "/home/jeorch/work/max/files/NHWA/max/nhwa匹配表.csv"
//        val acc_match_file = "/home/jeorch/work/max/files/NHWA/max/match_acc.csv"
//        val area_match_file = "/home/jeorch/work/max/files/NHWA/max/match_area.csv"
//        val market_match_file = "/home/jeorch/work/max/files/NHWA/max/match_market.csv"
//
//        val output_path = "/home/jeorch/work/max/files/NHWA/spark-result"
//
//        val df_max = driver.csv2RDD(max_result_file)
//            val gb_test = df_max.select(df_max("Panel_ID"),df_max("Date"),df_max("City"),df_max("Product"),df_max("f_sales").cast("double"),df_max("f_units").cast("double")).groupBy("Date","City","Product")
//            val df_gb_sum = gb_test.agg(("f_units","sum"),("f_sales","sum"),("Panel_ID","first"))
//
//            val df_filter = df_gb_sum.filter("Product <> '多美康片剂15MG10上海罗氏制药有限公司'")
//
//        val df_match_hospital = driver.csv2RDD(hospital_match_file)
//        val df_match_nhwa = driver.csv2RDD(nhwa_match_file).select("药品名称_标准","商品名_标准","药品规格_标准","剂型_标准","生产企业_标准","min1_标准","Pack_ID","商品名+SKU","毫克数").distinct()
//        val df_match_acc = driver.csv2RDD(acc_match_file)
//        val df_match_area = driver.csv2RDD(area_match_file)
//        val df_match_market = driver.csv2RDD(market_match_file)
//
//        val df_new1 = df_filter.join(df_match_hospital, df_filter("first(Panel_ID)") === df_match_hospital("Panel_ID"), "left").drop(df_match_hospital("City")).drop(df_filter("first(Panel_ID)"))
//
//        val df_new2 = df_new1.join(df_match_nhwa, df_new1("Product") === df_match_nhwa("min1_标准"), "left")
//
//        val df_result1 = df_new2.select("Panel_ID","Date","City","Product","商品名+SKU","商品名_标准","生产企业_标准","省份","City Tier","药品名称_标准","药品规格_标准","剂型_标准","sum(f_units)","sum(f_sales)","毫克数").withColumnRenamed("sum(f_units)","sum(Units)").withColumnRenamed("sum(f_sales)","sum(Sales)")
//
//        val df_result2 = df_result1.withColumn("销售毫克数",df_result1("毫克数").*(df_result1("sum(Units)"))).drop(df_result1("毫克数"))
//
//        val df_result3 = df_result2.join(df_match_acc, df_result2("药品名称_标准") === df_match_acc("分子名"), "left").drop("药品名称")
//
//        val df_result4 = df_result3.join(df_match_area, df_result3("省份") === df_match_area("省份") && df_result3("ACC1/ACC2") === df_match_area("ACC1/ACC2"), "left").drop(df_match_area("省份")).drop(df_match_area("ACC1/ACC2"))
//        val df_temp1 = df_result4.withColumnRenamed("分子名", "molecule").withColumnRenamed("区域", "area")
//                .withColumnRenamed("商品名+SKU", "p_sku").withColumnRenamed("商品名_标准", "p_standard")
//                .withColumnRenamed("生产企业_标准", "company_standard").withColumnRenamed("省份", "province")
//                .withColumnRenamed("City Tier", "City_Tier").withColumnRenamed("ACC1/ACC2", "ACC")
//                .withColumnRenamed("药品规格_标准", "medicine_standard").withColumnRenamed("剂型_标准", "dosage_form")
//                .withColumnRenamed("sum(Sales)", "Sales").withColumnRenamed("sum(Units)", "Units")
//                .withColumnRenamed("销售毫克数", "SalesMG")
//
//        val df_result5 = df_temp1.filter("area <> 'null'")
//            .selectExpr("Date","City","Product","p_sku","p_standard","company_standard","province","City_Tier","area","molecule","ACC","medicine_standard","dosage_form","Units","Sales","SalesMG","concat(molecule,area) as power")
//                .union(df_temp1.filter("area is null")
//                    .selectExpr("Date","City","Product","p_sku","p_standard","company_standard","province","City_Tier","area","molecule","ACC","medicine_standard","dosage_form","Units","Sales","SalesMG","concat(molecule,'NA') as power")
//                )
//
//        val df_result6 = df_result5.withColumn("year", df_result5("Date").substr(0, 4)).withColumn("month", df_result5("Date").substr(5, 6))
//
//        val df_result7 = df_result6.withColumn("market_num",df_result6("City_Tier").*(0).+(1).cast("int"))
//
//        val df_result8 = df_result7.join(df_match_market, df_result7("market_num") === df_match_market("market_num"), "left").drop("market_num").select("Date","City","Product","p_sku","p_standard","company_standard","year","month","province","City_Tier","market_name","area","molecule","ACC","power","medicine_standard","dosage_form","Units","Sales","SalesMG")
//
////        val df_fianl = df_result8.select(df_result8("Date").as("时间"),df_result8("City").as("城市")
////                ,df_result8("Product").as("min2"),df_result8("p_sku").as("商品名+SKU"),
////                df_result8("p_standard").as("商品名_标准"),df_result8("company_standard").as("生产企业_标准"),
////                df_result8("year").as("年"),df_result8("month").as("月"),
////                df_result8("province").as("省份"),df_result8("City_Tier").as("City.Tier"),
////                df_result8("market_name").as("市场I"),df_result8("area").as("区域"),
////                df_result8("molecule").as("分子名"),df_result8("ACC").as("ACC1/ACC2"),
////                df_result8("power").as("power"),df_result8("medicine_standard").as("规格"),
////                df_result8("dosage_form").as("剂型"),df_result8("Units").as("销售数量"),
////                df_result8("Sales").as("销售金额"),df_result8("SaleMG").as("销售毫克数"))
//
//        val saveOptions = Map("header" -> "true", "path" -> s"${output_path}")
//        df_result8.coalesce(1).write.format("csv").mode(SaveMode.Overwrite).options(saveOptions).save()
//        driver.ss.stop()
//        //val model = "时间,城市,min2,商品名+SKU,商品名_标准,生产企业_标准,年,月,省份,City.Tier,市场I,区域,分子名,ACC1/ACC2,power,规格,剂型,销售数量,销售金额,销售毫克数"
//            //min2 = Product
//            //分子名 = 药品名称
//
//            //解决
//            //市场I,年,月,区域,ACC1/ACC2,销售毫克数 = Units * 毫克数
//            //power = 分子名 + 区域
//    }

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

//    test("Test DriverNHWA") {
//        val driverNHWA = DriverNHWA()
//        driverNHWA.generateFianlFile()
//    }

//    test("Test getResultFileFullPath"){
//        val driverNHWA = DriverNHWA()
//        val test = driverNHWA.getResultFileFullPath("/home/jeorch/work/max/files/NHWA/spark-result")
//        println(s"***********${test}")
//    }

}
