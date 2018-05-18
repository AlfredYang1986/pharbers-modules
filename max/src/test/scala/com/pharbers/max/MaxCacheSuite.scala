//package com.pharbers.max
//
//import com.pharbers.calc.phMaxScheduleJob
//import com.pharbers.driver.PhRedisDriver
//import com.pharbers.spark.phSparkDriver
//import org.scalatest.FunSuite
//
///**
//  * Created by jeorch on 18-5-8.
//  */
//class MaxCacheSuite extends FunSuite {
//
//    private val mongodbHost = "127.0.0.1"
//    private val mongodbPort = "27017"
//    private val databaseName = "Max_Test"
//    private val sd = phSparkDriver()
//
//    test("spark read mongo"){
//        sd.sqc.setConf("spark.mongodb.output.uri",s"mongodb://$mongodbHost:$mongodbPort/")
//        val mongoRDD = sd.mongo2RDD("127.0.0.1","27017","Max_Test","Allelock_Factorized_Units&Sales_WITH_OT1712")
//        val myDF = mongoRDD.toDF()
//        myDF.show(10)
//    }
//
//    test("spark write mongo"){
//        val mongoRDD = sd.mongo2RDD("127.0.0.1","27017","Max_Test","Allelock_Factorized_Units&Sales_WITH_OT1712")
//        val myDF = mongoRDD.toDF()
//        sd.dataFrame2Mongo(myDF,mongodbHost,mongodbPort,databaseName,"testCollection","append")
//    }
//
//    test("max schedule job"){
//        //TODO:(标记人:Jeorch,标记时间:20180515,预计完成时间:上线时预设管理员信息在数据库中,暂时以redis做测试)
//        val rd = new PhRedisDriver()
//        val user = "jeorch"
//        rd.addString("MaxAdmin", user)
//        rd.expire("MaxAdmin",60*30) //过期时间30min
//        val result = phMaxScheduleJob(user).start
//        println(result)
//    }
//
//}
