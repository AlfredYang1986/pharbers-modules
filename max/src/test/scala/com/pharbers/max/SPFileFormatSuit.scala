package com.pharbers.max

import com.pharbers.panel.nhwa.phNhwaCalcYMJob
import org.scalatest.FunSuite

class SPFileFormatSuit extends FunSuite {

    implicit def progressFunc(progress: Double, flag: String) : Unit = Unit
    test("Spark File Convert") {
        phNhwaCalcYMJob("resource/test-01.xlsx", "result/nhwa/ym").perform()
    }

//    test("edit distance") {
//        val a = "aabbcc"
//        val b = "aabcc"
//
//        ph_alg.edit_distance(a, b) === 1
//        ph_alg.edit_distance("ATC编码", "ATC码") === 1
//    }

//    object tf extends NhwaPanelActions
//    test("Spark File Convert") {
//        tf.perform(NULLArgs)
//    }
}
