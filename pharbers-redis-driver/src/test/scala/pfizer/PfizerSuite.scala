//package pfizer
//
//import com.pharbers.driver.redis.phRedisDriver
//import com.redis.RedisClient
//import org.scalatest.FunSuite
//
///**
//  * Created by jeorch on 17-11-17.
//  */
//class PfizerSuite extends FunSuite {
//
//    test("Test Redis sadd"){
//        val phSetDriver = phRedisDriver().phSetDriver
//        1 to 100000 foreach { i =>
//            phSetDriver.sadd("segment", s"${i}String")
//        }
//    }
//
//    test("Test Read Redis sadd"){
//        val phSetDriver = phRedisDriver().phSetDriver
//
//        val result = phSetDriver.smembers("segment")
//        println(s"result.size = ${result.size}")
//        result.foreach(x => println(s"&& = ${x}"))
//    }
//
//    test("Test Delete Redis sadd"){
//        val phSetDriver = phRedisDriver().phSetDriver
//
//        phRedisDriver().del("segment")
//    }
//
//}
