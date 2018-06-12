package com.pharbers.unitTest

import java.util.UUID
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.duration._
import com.pharbers.builder.MarketTable
import com.pharbers.pactions.actionbase.{MapArgs, StringArgs}
import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import com.pharbers.spark.phSparkDriver
import scala.concurrent.Await

case class startTest() {

    val system = ActorSystem("unitTest")
    implicit val t: Timeout = 1200 minutes
    val path = "/mnt/config/Pfizer线上MAX对比文件/"
    val companyLst: List[Map[String, String]] =
        new MarketTable{}.marketTable
            .filter(_("company") == "5b028f95ed925c2c705b85ba")

    def doTest(): List[String] = companyLst.map{ c =>
        val args = Map(
            "company" -> c("company"),
            "mkt" -> c("market"),
            "user" -> "user",
            "job_id" -> UUID.randomUUID().toString,
//            "cpa" -> "pfizer/1802 CPA.xlsx",
//            "gycx" -> "pfizer/1802 GYC.xlsx",
//            "ym" -> "201802",
//            "offlineResult" -> "/mnt/config/INF_MAX_Result.xlsx"
            "cpa" -> c("cpa_file"),
            "gycx" -> c("gycx_file"),
            "ym" -> c("ym"),
            "offlineResult" -> (path + c("offlineResult"))
        )

        val testHeader: ActorRef = system.actorOf(UnitTestHeader.props())
        val r = testHeader ? UnitTestHeader.testJob(args)
//        Await.result(r.mapTo[String], t.duration)
        Await.result(r.mapTo[String], t.duration)
        //val result = Await.result(future, timeout.duration).asInstanceOf[String]
//        Await.result(r.mapTo[String], t.duration).asInstanceOf[MapArgs].get("result_check").asInstanceOf[DFArgs].get
    }
    
    def writeTotalResult(): StringArgs = {
        val uuid = UUID.randomUUID().toString
        doTest().map{f =>
            val sparkDriver = phSparkDriver()
            sparkDriver.sqc.read.format("com.databricks.spark.csv")
                    .option("header", "true") //这里如果在csv第一行有属性的话，没有就是"false"
                    .option("inferSchema", true.toString) //这是自动推断属性列的数据类型。
                    .option("delimiter", 31.toChar.toString)
                    .load("/mnt/config/result/" + f) //文件的路径
        }.reduce((totalResult, f) => totalResult union f).coalesce(1).write
                .format("csv")
                .option("header", value = true)
                .option("delimiter", 31.toChar.toString)
                .save("/mnt/config/result/" + uuid)
        println("最终结果" + uuid)
        StringArgs(uuid)
    }
    
    def fileFillter(): Unit ={
    
    }
}

object UnitTestHeader {
    def props(): Props = Props[UnitTestHeader]
    case class testJob(args: Map[String, String])
}

class UnitTestHeader extends Actor {
    override def receive: Receive = {
        case UnitTestHeader.testJob(args) =>
            sender() ! resultCheckJob(args)(this)
                    .perform(MapArgs(Map().empty))
                    .asInstanceOf[MapArgs]
                    .get("checkResult")
                    .asInstanceOf[StringArgs].get
//            sender() ! "test success"
    }
}
