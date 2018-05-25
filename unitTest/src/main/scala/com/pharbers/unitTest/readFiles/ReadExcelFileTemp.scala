package com.pharbers.unitTest.readFiles

import java.io.FileInputStream
import java.text.DecimalFormat

import org.apache.poi.xssf.usermodel.XSSFWorkbook
import scala.math.BigDecimal

class ReadExcelFileTemp(xls_file: String) {
    val ins = new FileInputStream(xls_file)
    val xssfWorkbook = new XSSFWorkbook(ins)
    val sheet = xssfWorkbook.getSheetAt(0)
    val lastRowNumber = sheet.getLastRowNum
    val startRow = 1
    val rows = lastRowNumber+1
    
    def getExcelResult: List[Map[String, String]] ={
        val excelList: List[Map[String, String]] = Range(startRow, rows).map(row => getResult(row)).toList
        excelList
    }
    
    def getArea(): List[Map[String, String]] ={
        val areaResult = Range(startRow, rows).map(row =>
            Map("areaType" -> sheet.getRow(row).getCell(7).toString,
                "area" -> sheet.getRow(row).getCell(2).toString
            )
        )
        areaResult.toList
    }
    
    def getResult(row: Int): Map[String, String] ={
        val decimalFormat = new DecimalFormat("###################.##################")
        Map(
            "hosptalCount" -> decimalFormat.format(sheet.getRow(row).getCell(3).getNumericCellValue),
            "productCount" -> decimalFormat.format(sheet.getRow(row).getCell(4).getNumericCellValue),
            "sales" -> sheet.getRow(row).getCell(5).toString,
            "units" -> sheet.getRow(row).getCell(6).toString
        )
    }
    
    def getAllResult(): List[Map[String, String]] ={
        val decimalFormat = new DecimalFormat("###################.##################")
        Range(startRow, rows).map(row =>
            Map(
                "areaType" -> sheet.getRow(row).getCell(7).toString,
                "area" -> sheet.getRow(row).getCell(2).toString,
                "hosptalCount" -> decimalFormat.format(sheet.getRow(row).getCell(3).getNumericCellValue),
                "productCount" -> decimalFormat.format(sheet.getRow(row).getCell(4).getNumericCellValue),
                "sales" -> sheet.getRow(row).getCell(5).toString,
                "units" -> sheet.getRow(row).getCell(6).toString
            )
        ).toList
    }
    
    def getExcelFileResult(): List[List[Map[String, String]]] ={
//        读取Excel文件，每500条一个List，返回值为List[List[Map[String, String]]]
        val nums = if(lastRowNumber%500==0) lastRowNumber/500 else lastRowNumber/500+1
        Range(0, nums).map(num =>getResultByNum(500*num, 500*num+500)).toList
    }
    
    def getResultByNum(startNum: Int, endNum: Int): List[Map[String, String]] ={
        endNum >=lastRowNumber match {
            case true => Range(startNum, lastRowNumber).map(row => getAllResult(row)).toList
            case false => Range(startNum, endNum).map(row => getAllResult(row)).toList
        }
    }
    
    def getAllResult(): List[Map[String, String]] ={
        val decimalFormat = new DecimalFormat("###################.##################")
        Range(startRow, rows).map(row =>
            Map(
                "areaType" -> sheet.getRow(row).getCell(7).toString,
                "area" -> sheet.getRow(row).getCell(2).toString,
                "hosptalCount" -> decimalFormat.format(sheet.getRow(row).getCell(3).getNumericCellValue),
                "productCount" -> decimalFormat.format(sheet.getRow(row).getCell(4).getNumericCellValue),
                "sales" -> sheet.getRow(row).getCell(5).toString,
                "units" -> sheet.getRow(row).getCell(6).toString
            )
        ).toList
    }
}

object ReadExcelFileTemp {
    def apply(xls_file: String) = new ReadExcelFileTemp(xls_file: String)
}
