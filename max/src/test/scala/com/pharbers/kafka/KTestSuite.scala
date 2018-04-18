package com.pharbers.kafka

import com.pharbers.chain.kafkaPushTopic
import com.pharbers.chain.kafkaLstTopics
import org.scalatest.FunSuite

class KTestSuite extends FunSuite{

//    test("kafka topic creation") {
//        case class createTopic() extends kafkaPushTopic
//        createTopic().pushTopic("alfredyang")
//    }

    test("kafka topic listing") {
        case class lstTopic() extends kafkaLstTopics
        lstTopic().lstTopics.foreach(println(_))
    }
}