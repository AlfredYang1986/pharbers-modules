import com.pharbers.spark.driver.phSparkDriver
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

        val output_path = "/home/jeorch/work/max/files/NHWA/new.csv"

        val df_max = driver.csv2RDD(max_result_file)
//        println(s"******************df_max = ${df_max.count()}")

        val df_match_hospital = driver.csv2RDD(hospital_match_file)
        val df_match_nhwa = driver.csv2RDD(nhwa_match_file)
        val df_match_acc = driver.csv2RDD(acc_match_file)
        val df_match_area = driver.csv2RDD(area_match_file)
        val df_match_market = driver.csv2RDD(market_match_file)

        val df_new1 = df_max.join(df_match_hospital, df_max("Panel_ID") === df_match_hospital("Panel_ID"), "left").drop(df_match_hospital("Panel_ID")).drop(df_match_hospital("City"))
//        df_new1.show()
//        println(s"******************df_new1 = ${df_new1.count()}")

        //条数不匹配，max_result=726540,new2=726750,问题出在new2
        val df_new2 = df_new1.join(df_match_nhwa, df_new1("Product") === df_match_nhwa("min1_标准"), "left")
//        df_new2.show()
//        println(s"******************df_new2 = ${df_new2.count()}")

        val df_result1 = df_new2.select("Panel_ID","Date","City","Product","商品名+SKU","商品名_标准","生产企业_标准","省份","City Tier","药品名称","规格","剂型_标准","Units","Sales")
//        df_result1.show()
//        println(s"******************df_result1 = ${df_result1.count()}")

        val df_unitsMs = df_new2.select(df_new2("Panel_ID"), df_new2("min1_标准"), df_new2("毫克数").*(df_new2("Units")))

        val df_result2 = df_result1.join(df_unitsMs, df_result1("Panel_ID") === df_unitsMs("Panel_ID") && df_result1("Product") === df_unitsMs("min1_标准"), "left").drop(df_unitsMs("min1_标准")).drop(df_unitsMs("Panel_ID"))
//        df_result2.show()
//        println(s"******************df_result2 = ${df_result2.count()}")

        val df_result3 = df_result2.join(df_match_acc, df_result2("药品名称") === df_match_acc("分子名"), "left").drop("药品名称")
//        df_result3.show()
//        println(s"******************df_result3 = ${df_result3.count()}")

        val df_result4 = df_result3.join(df_match_area, df_result3("省份") === df_match_area("省份"), "left").drop(df_match_area("省份"))
//        println(s"******************df_result4 = ${df_result4.count()}")
        val df_temp = df_result4.withColumnRenamed("分子名", "molecule").withColumnRenamed("区域", "area")

        val df_power = df_temp.selectExpr("Panel_ID", "Product", "concat(molecule,area) as power")

        val df_result5 = df_result4.join(df_power, df_result4("Panel_ID") === df_power("Panel_ID") && df_result4("Product") === df_power("Product"), "left").drop(df_power("Product")).drop(df_power("Panel_ID"))
//        df_result5.show()
//        println(s"******************df_result5 = ${df_result5.count()}")

        val df_result6 = df_result5.withColumn("year", df_result5("Date").substr(0, 4)).withColumn("month", df_result5("Date").substr(5, 6))
//            df_result6.show()

        val df_result7 = df_result6.withColumn("market_num",df_result6("City Tier").*(0).+(1).cast("int"))

        val df_result8 = df_result7.join(df_match_market, df_result7("market_num") === df_match_market("market_num"), "left").drop("market_num", "Panel_ID").select("Date","City","Product","商品名+SKU","商品名_标准","生产企业_标准","year","month","省份","City Tier","market_name","区域","分子名","ACC1/ACC2","power","规格","剂型_标准","Units","Sales","(毫克数 * Units)")
        df_result8.show()
//        println(s"******************df_result8 = ${df_result8.count()}")

//        df_result8.write.format("csv").save(output_path)
        df_result8.coalesce(1).write.format("csv").save(output_path)
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
