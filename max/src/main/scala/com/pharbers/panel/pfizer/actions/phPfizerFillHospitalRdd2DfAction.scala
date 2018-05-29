package com.pharbers.panel.pfizer.actions

import com.pharbers.pactions.actionbase._
import com.pharbers.spark.phSparkDriver

/**
  * Created by jeorch on 18-4-26.
  */
object phPfizerFillHospitalRdd2DfAction {
    def apply(defaultArgs: String, name: String = "phPfizerFillHospitalRdd2DfAction"): pActionTrait =
        new phPfizerFillHospitalRdd2DfAction(StringArgs(defaultArgs), name)
}

class phPfizerFillHospitalRdd2DfAction(override val defaultArgs: pActionArgs, override val name: String) extends pActionTrait {

    override def perform(args : pActionArgs): pActionArgs = {

        val sparkDriver = phSparkDriver()

        val rdd_temp = sparkDriver.sc.textFile(defaultArgs.asInstanceOf[StringArgs].get)
        val fillHospital = rdd_temp
            .map(_.split(31.toChar.toString))
            .map(row => FillHospitalFormat(
                row(0).replaceAll(34.toChar.toString," ").trim,
                row(1).replaceAll(34.toChar.toString," ").trim,
                row(2).replaceAll(34.toChar.toString," ").trim,
                row(3).replaceAll(34.toChar.toString," ").trim,
                row(4).replaceAll(34.toChar.toString," ").trim,
                row(5).replaceAll(34.toChar.toString," ").trim,
                row(6).replaceAll(34.toChar.toString," ").trim,
                row(7).replaceAll(34.toChar.toString," ").trim,
                row(8).replaceAll(34.toChar.toString," ").trim,
                row(9).replaceAll(34.toChar.toString," ").trim,
                row(10).replaceAll(34.toChar.toString," ").trim,
                row(11).replaceAll(34.toChar.toString," ").trim,
                row(12).replaceAll(34.toChar.toString," ").trim
            ))
        val df = sparkDriver.ss.createDataFrame(fillHospital)
        DFArgs(df)
    }

}

case class FillHospitalFormat(CITY: String,
                              YEAR: String,
                              MONTH: String,
                              HOSPITAL_CODE: String,
                              MOLE_NAME: String,
                              PRODUCT_NAME: String,
                              PACK_DES: String,
                              PACK_NUMBER: String,
                              VALUE: String,
                              STANDARD_UNIT: String,
                              APP2_COD: String,
                              APP1_COD: String,
                              CORP_NAME: String
                             )