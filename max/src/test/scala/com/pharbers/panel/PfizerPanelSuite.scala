//package com.pharbers.panel
//
//import java.text.SimpleDateFormat
//import java.util.Date
//
//import com.pharbers.pactions.actionbase.MapArgs
//import com.pharbers.panel.pfizer.phPfizerPanelJob
//import org.scalatest.FunSuite
//
///**
//  * Created by jeorch on 18-4-19.
//  */
//class PfizerPanelSuite extends FunSuite {
//
//    test("pfizer generate panel"){
//        /**
//          * 已通过1712月的对数的市场:
//          * INF(3462),CNS_Z(5844),AI_S(7013)
//          * (罗然他们给我们的12月比对panel中有If_Panel_All===0的一些医院,
//          * 需要删了这些医院条数后进行比对)
//          *
//          * 有问题的大多都是分拆出来的市场:
//          * AI_R_zith(头孢孟多问题),
//          */
//        val mkt = "PAIN_other"
//
//        val dateformat = new SimpleDateFormat("MM-dd HH:mm:ss")
//        println(s"生成panel测试开始时间" + dateformat.format(new Date()))
//        println()
//
//        val result = phPfizerPanelJob("/mnt/config/Client/pfizer/1712 CPA.xlsx", "/mnt/config/Client/pfizer/1712 GYC.xlsx", "201712", s"${mkt}").perform().asInstanceOf[MapArgs].get("phSavePanelJob").get
//        println("result = " + result)
//
//        println()
//        println(s"生成panel测试结束时间" + dateformat.format(new Date()))
//    }
//
//}
