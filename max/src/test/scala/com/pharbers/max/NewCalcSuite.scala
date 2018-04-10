package com.pharbers.max

import com.pharbers.calc.phMaxActions
import org.scalatest.FunSuite

/**
  * Created by jeorch on 18-4-10.
  */
class NewCalcSuite extends FunSuite{

    test("test group"){
        val args = Map(
            "uid" -> "testUid",
            "panel" -> "8543ae17-5be8-4d40-ba83-ebffc5b2c21a.csv"
        )
        phMaxActions(args).perform()
    }

}
