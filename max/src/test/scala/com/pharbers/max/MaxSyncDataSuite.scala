//package com.pharbers.max
//
//import java.util.Base64
//import org.scalatest.FunSuite
//
///**
//  * Created by jeorch on 18-5-23.
//  */
//class MaxSyncDataSuite extends FunSuite {
//
//    val testKey = "company" + "ym" + "mkt"
//
//    test("Base64 from java8"){
//        val jdkEncoder = Base64.getEncoder
//        val jdkDecoder = Base64.getDecoder
//        val testEncodeStr = jdkEncoder.encodeToString(testKey.getBytes)
//        println(s"加密后的结果$testEncodeStr")
//        val testDecodeStr = new String(jdkDecoder.decode(testEncodeStr))
//        println(s"解密后的结果$testDecodeStr")
//    }
//
//}
