package com.pharbers.unitTest.getResult

import scala.math.abs

case class compareResult() {
    def isSame(excelResult: Map[String, String], csvResult: Map[String, String]): Boolean ={
        excelResult.keys.map(key => getDifference(excelResult(key),csvResult(key))).find(_ == false).getOrElse(true)
    }
    
    def getDifference(eValue: String, cValue: String): Boolean ={
        if(abs(eValue.toDouble-cValue.toDouble)<0.0001) true else false
    }
}
