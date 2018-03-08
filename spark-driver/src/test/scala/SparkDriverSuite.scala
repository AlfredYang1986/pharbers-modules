//import com.pharbers.spark.driver.phSparkDriver
//import org.scalatest.FunSuite
//
///**
//  * Created by clock on 18-2-26.
//  */
//class SparkDriverSuite extends FunSuite with phMongoConfig{
//    val driver =  phSparkDriver()
//
//    test("Test read mongo") {
//        def users = driver.mongo2RDD(mongodbHost, mongodbPort, "baby_time_test", "users")
//        def services = driver.mongo2RDD(mongodbHost, mongodbPort, "baby_time_test", "services")
//        println(users)
//        println(services)
//    }
//
//    test("Test read csv"){
//        val file_path = "file:///home/jeorch/jeorch/test/file_test/text.csv"
//
//        val rdd = driver.csv2RDD(file_path, delimiter = 31.toChar.toString, true)
////        rdd.foreach(x => println(x))
////        rdd.printSchema()
//        rdd.show(false)
////        println(rdd.count())
//    }
//}
