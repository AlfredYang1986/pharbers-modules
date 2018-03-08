import com.pharbers.spark.driver.phSparkDriver
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types._
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

//    test("Test join") {
//        val file1 = "/home/jeorch/work/max/files/NHWA/test.csv"
//        val file2 = "/home/jeorch/work/max/files/NHWA/test2.csv"
//        val df1 = driver.csv2RDD(file1)
//        val df2 = driver.csv2RDD(file2)
////        df1.show()
////        df2.show()
//        df1.join(df2, df1("Panel_ID")===df2("Panel_ID"), "left").show()
//    }

    test("Test Max john") {
//        val max_result_file = "/home/jeorch/work/max/files/NHWA/max/max 結果.csv"
        val max_result_file = "/home/jeorch/work/max/files/NHWA/max/max_result.csv"
        val hospital_match_file = "/home/jeorch/work/max/files/NHWA/max/交付格式医院匹配表.csv"
        val nhwa_match_file = "/home/jeorch/work/max/files/NHWA/max/nhwa匹配表.csv"

        val df_max = driver.csv2RDD(max_result_file)
        val df_match_hospital = driver.csv2RDD(hospital_match_file)
        val df_match_nhwa = driver.csv2RDD(nhwa_match_file)

        val df_new1 = df_max.join(df_match_hospital, df_max("Panel_ID")===df_match_hospital("Panel_ID"), "left")
        val df_new2 = df_new1.join(df_match_nhwa, df_new1("Product")===df_match_nhwa("min1_标准"), "left")

        val model = "时间,城市,min2,商品名+SKU,商品名_标准,生产企业_标准,年,月,省份,City.Tier,市场I,区域,分子名,ACC1/ACC2,power,规格,剂型,销售数量,销售金额,销售毫克数"
        //min2 = Product
        //分子名 = 药品名称
        //power = 分子名 + 区域
        //销售毫克数 = f_units * 毫克数

        //缺少 年,月,市场I,区域,ACC1/ACC2,

        //ambiguity ：City & City.Tier
        df_new2.select("Date","Product","商品名+SKU","商品名_标准","生产企业_标准","省份","药品名称","规格","剂型_标准","f_units","f_sales").show()
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
