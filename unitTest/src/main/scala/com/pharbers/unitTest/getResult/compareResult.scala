package com.pharbers.unitTest.getResult

import java.util

import scala.math.abs

case class compareResult() {
    val keyList: List[String] = List("hosptalCount", "productCount", "sales", "units")
    def isSame(excelResult: Map[String, String], csvResult: Map[String, String]): Boolean ={
        println(excelResult)
        println(csvResult)
        keyList.map(key => getDifference(excelResult(key),csvResult(key))).find(_ == false).getOrElse(true)
    }
    
    def getDifference(eValue: String, cValue: String): Boolean ={
        if(abs(eValue.toDouble-cValue.toDouble)<0.0001) true else false
    }

    def getDifferencePoint(excelResult: Map[String, String], csvResult: Map[String, String]): String ={
        val differencePoints = keyList.foreach(key => getDifference(excelResult(key),csvResult(key)) match {
            case true => ""
            case false => key
        })
        val difstr = differencePoints.toString
        difstr.substring(5,difstr.length - 1)
    }
}
