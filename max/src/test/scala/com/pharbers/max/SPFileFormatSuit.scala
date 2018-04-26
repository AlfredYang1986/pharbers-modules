//package com.pharbers.max
//
//import org.scalatest.FunSuite
//import com.pharbers.panel.nhwa.phNhwaPanelJob
//
//class SPFileFormatSuit extends FunSuite {
//    implicit def progressFunc(progress: Double, flag: String) : Unit = Unit
//
//    test("nhwa calc ym") {
//        phNhwaCalcYMJob("resource/test-02.xlsx", "result/nhwa/ym").perform()
//    }
//
//    test("nhwa panel generator") {
//        phNhwaPanelJob("/mnt/config/Client/171215恩华2017年10月检索.xlsx", "result/nhwa/panel").perform()
//    }
//}
