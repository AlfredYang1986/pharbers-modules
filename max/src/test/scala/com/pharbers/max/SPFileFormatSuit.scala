package com.pharbers.max

import com.pharbers.panel.nhwa.phNhwaCalcYMJob
import org.scalatest.FunSuite

class SPFileFormatSuit extends FunSuite {

    implicit def progressFunc(progress: Double, flag: String) : Unit = Unit
    test("Spark File Convert") {
        phNhwaCalcYMJob("resource/test-02.xlsx", "result/nhwa/ym").perform()
    }

}
