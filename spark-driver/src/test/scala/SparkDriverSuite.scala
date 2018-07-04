//import com.mongodb.spark.MongoSpark
//import com.mongodb.spark.config.ReadConfig
//import com.pharbers.spark.phSparkDriver
//import org.scalatest.FunSuite
//
///**
//  * Created by clock on 18-2-26.
//  */
//class SparkDriverSuite extends FunSuite with phMongoConfig {
//
//
//    test("Test read mongo") {
//        val driver =  phSparkDriver()
//        def users = driver.mongo2RDD(mongodbHost, mongodbPort, "baby_time_test", "users")
//        def services = driver.mongo2RDD(mongodbHost, mongodbPort, "baby_time_test", "services")
//        println(users)
//        println(services)
//    }
//
//    test("Test read csv"){
//        val driver =  phSparkDriver()
//        val file_path = "/home/jeorch/work/max_file/Max/a95e32b2-9945-4bd2-8b42-32271b19259a60b738de-0125-4959-a883-63c3e8c3d5d4"
//
//        val rdd = driver.csv2RDD(file_path, delimiter = 31.toChar.toString)
////        rdd.foreach(x => println(x))
//        rdd.printSchema()
////        rdd.show(false)
////        println(rdd.count())
//    }
//
//    test("spark read mongo"){
//        val sd = phSparkDriver()
//        val mongoRDD = sd.mongo2RDD("127.0.0.1","27017","Max_Test","Allelock_Factorized_Units&Sales_WITH_OT1712")
//        val mongoDF = mongoRDD.toDF()
//        mongoDF.show(10)
//    }
//
//    test("spark write mongo"){
//        val databaseName = "Max_Test"
//        val sd = phSparkDriver()
//
//        val mongoRDD = sd.mongo2RDD("127.0.0.1","27017","Max_Test","Allelock_Factorized_Units&Sales_WITH_OT1712")
//        val mongoDF = mongoRDD.toDF()
//
//        sd.dataFrame2Mongo(mongoDF, mongodbHost, mongodbPort, databaseName, "testColl", "append")
//    }
//
//}
