package com.pharbers.max

import com.pharbers.panel.nhwa.phNhwaCalcYMJob
import com.pharbers.panel.nhwa.phNhwaPanelJob
import org.scalatest.FunSuite

class SPFileFormatSuit extends FunSuite {

    implicit def progressFunc(progress: Double, flag: String) : Unit = Unit
//    test("nhwa calc ym") {
//        phNhwaCalcYMJob("resource/test-02.xlsx", "result/nhwa/ym").perform()
//    }

    test("nhwa panel generator") {
        phNhwaPanelJob("resource/nhwa/test-02.xlsx", "resource/midTmp", "result/nhwa/panel").perform()
    }

}
