//package com.pharbers.common.kafka
//
//import akka.actor.ActorSystem
//import org.scalatest.FunSuite
//import java.util.concurrent.Executors
//
//import com.pharbers.channel.chanelImpl.callJobConsumer
//
//class KTestSuite extends FunSuite{
//
////    test("kafka topic creation") {
////        case class createTopic() extends kafkaPushTopic
////        createTopic().pushTopic("alfredyang")
////    }
//
////    test("kafka topic listing") {
////        case class lstTopic() extends kafkaLstTopics
////        lstTopic().lstTopics.foreach(println(_))
////    }
//
////    test("kafka push record to topic alfredyang") {
////        import ch.precord
////        ch.pushRecord(Map("id" -> "abcde",
////                          "name" -> "abcde",
////                          "stages" -> "alfred",
////                          "progress" -> 90.asInstanceOf[Number]))
////
////        Thread.sleep(5000)
////    }
//
//    test("kafka Consumer for topic max_callJob") {
//
//        val asys = ActorSystem("test")
//        val ch = callJobConsumer("abcde")(asys)
//
//        val tmp = Executors.newFixedThreadPool(1).submit(ch)
//
//        Thread.sleep(50000)
//    }
//}