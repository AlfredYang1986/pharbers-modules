//package com.pharbers.kafka
//
//import java.util.concurrent.Executors
//
//import akka.actor.ActorSystem
//import com.pharbers.channel.chanelImpl.{kafkaConsumer, kafkaLstTopics, kafkaPushRecord}
//import com.pharbers.channel.msgChannel
//import org.scalatest.FunSuite
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
//    implicit val asys = ActorSystem("test")
//    val ch = msgChannel("alfredyang")(asys)
//
//    test("kafka push record to topic alfredyang") {
//        import ch.precord
//        ch.pushRecord(
//            Map(
//                "id" -> "abcde",
//                "name" -> "abcde",
//                "stages" -> "alfred",
//                "progress" -> 90.asInstanceOf[Number]
//            )
//        )
//
//        Thread.sleep(5000)
//    }
//
//    val tmp = Executors.newFixedThreadPool(1)
//    tmp.submit(ch)
//}