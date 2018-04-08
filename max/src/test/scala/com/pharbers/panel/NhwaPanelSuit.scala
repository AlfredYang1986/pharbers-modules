package com.pharbers.panel

import org.scalatest.FunSuite
import com.pharbers.panel.nhwa.{phNhwaCalcYMJob, phNhwaPanelJob}

class NhwaPanelSuit extends FunSuite {
    implicit def progressFunc(progress: Double, flag: String) : Unit = Unit

    test("nhwa calc ym") {
        val result = phNhwaCalcYMJob("/mnt/config/Client/180211恩华17年1-12月检索.xlsx").perform().get
        println(result)
    }

    test("nhwa panel generator") {
        phNhwaPanelJob("/mnt/config/Client/171215恩华2017年10月检索.xlsx", "result/nhwa/panel").perform()
    }

}
