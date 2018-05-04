package com.pharbers.unitTest.getResult

import com.pharbers.unitTest.builder.{CompanyParamter, CompanyParamterFactory}
import com.pharbers.unitTest.readFiles.{ReadCSVFile, ReadExcelFile, readXmlFile}

import scala.xml.XML

object test extends App {
    //    val a = ReadExcelFile().getResult(3)
    //    println(a)
    
    //    val result = GetResult().getResult
    //    val csvResult = result.head
    //    val excelResult = result.last
    //    println(csvResult)
    //    println(excelResult)
    
    
    
    val result = GetResult().getResult
    for(i: Int <- result.indices){
        val csvResult = ReadCSVFile(result(i)("maxResult"), ReadExcelFile(result(i)("resultMatch_file")).getArea()).getCsvResult()
        val excelResult = ReadExcelFile(result(i)("resultMatch_file")).getExcelResult
        for (j: Int <- csvResult.indices){
            println(compareResult().isSame(excelResult(j),csvResult(j)))
        }
    }
}
