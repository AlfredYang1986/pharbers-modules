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
//        val file_path = "file:///mnt/config/a.csv"
//        val df = driver.csv2RDD(file_path)
//        df.show(false)
//    }
//
//    test("spark read mongo"){
//        val sd = phSparkDriver()
//        val mongoRDD = sd.mongo2RDD("127.0.0.1","27017","Max_Test","Allelock_Factorized_Units&Sales_WITH_OT1712")
//        val mongoDF = mongoRDD.toDF()
//        mongoDF.show(10)
//    }
//
//    test("spark read mongo all collections"){
//        /**
//          * Wrong function!To be corrected!
//          */
//        val databaseName = "Max_Test"
//        val sd = phSparkDriver()
//        val sc = sd.sc
//        val readConfig = ReadConfig(Map(
//            "spark.mongodb.input.uri" -> s"mongodb://$mongodbHost:$mongodbPort/$databaseName.*",
//            "readPreference.name" -> "secondaryPreferred")
//        )
//        val test = MongoSpark.load(sc, readConfig = readConfig)
//        println(test.count())
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
