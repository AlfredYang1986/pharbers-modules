import com.pharbers.nhwa.DriverNHWA
import com.pharbers.spark.driver.phSparkDriver
import org.apache.spark.sql.{SaveMode, SparkSession}
import org.scalatest.FunSuite
/**
  * Created by jeorch on 18-3-6.
  */
class MaxFactorySuite extends FunSuite {

//    val driver =  phSparkDriver()

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
//
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
//
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
//
//    test ("Test sqc load/save") {
//        val file_path = "/home/jeorch/work/max/files/NHWA/max 結果.csv"
//        val t = driver.sqc.load(file_path)
//
//        t.select("Panel_ID", "Date", "City")
//    }

//    test("Test DriverNHWA generateDeliveryFileFromMongo") {
//        val driverNHWA = DriverNHWA()
//        driverNHWA.generateDeliveryFileFromMongo("Max_Cores","8ee0ca24796f9b7f284d931650edbd4bcf811a08-8833-4aed-8ec7-6936894a91e3")
//    }

//    test("Test DriverNHWA generateDeliveryFileFromCSV") {
//        val driverNHWA = DriverNHWA()
//        val filePath = driverNHWA.generateDeliveryFileFromCSV("/mnt/config/FileBase/8ee0ca24796f9b7f284d931650edbd4b/max_result.csv")
//        println(s"*********${filePath}")
//    }

//    test("Move2ExportFolder"){
//        val driverNHWA = DriverNHWA()
//        driverNHWA.move2ExportFolder("/mnt/config/FileBase/8ee0ca24796f9b7f284d931650edbd4b/Delivery/part-r-00000-e3c3429a-e981-4d9f-b12c-15a5195e4882.csv",
//            "/home/jeorch/jeorch/test/file_test/nhwa-result.csv")
//    }

//    test ("Mutil generate") {
//        val driverNHWA = DriverNHWA()
//
//        val list = List(1,2,3)
//        val listDF = list.map( x =>
//            driverNHWA.generateDeliveryFileFromMongo("Max_Cores","8ee0ca24796f9b7f284d931650edbd4bf913d435-54cb-4380-bda9-0ccbe0e9afe5")
//            )
//        val fileName = driverNHWA.save2File(driverNHWA.unionDataFrameList(listDF))
//        println(s"***************${fileName}")
//
//        driverNHWA.closeSparkSession
//    }

    test ("set encoding") {
        val driverNHWA = DriverNHWA()
        val df = driverNHWA.generateDeliveryFileFromMongo("Max_Cores","8ee0ca24796f9b7f284d931650edbd4bbf4575e1-e6da-4d23-b6b1-b3d3e5b66a2d")
        val fileName = driverNHWA.save2File(df)
        println(s"***************${fileName}")
        driverNHWA.closeSparkSession
    }

}
