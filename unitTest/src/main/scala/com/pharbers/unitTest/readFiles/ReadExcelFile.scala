package com.pharbers.unitTest.readFiles

import java.io.FileInputStream
import java.text.DecimalFormat

import org.apache.poi.xssf.usermodel.XSSFWorkbook
import scala.math.BigDecimal

class ReadExcelFile(xls_file: String) {
    val ins = new FileInputStream(xls_file)
    val xssfWorkbook = new XSSFWorkbook(ins)
    val sheet = xssfWorkbook.getSheetAt(0)
    val lastRowNumber = sheet.getLastRowNum
    def getExcelResult: List[Map[String, String]] ={
        val excelList: List[Map[String, String]] = Range(1, lastRowNumber+1).map(row => getResult(row)).toList
        excelList
    }
    def getArea(): List[Map[String, String]] ={
        val rows = lastRowNumber+1
        val areaResult = Range(2, rows).map(row =>
                Map("areaType" -> sheet.getRow(row).getCell(3).toString,
                    "area" -> sheet.getRow(row).getCell(2).toString
                )
        )
        areaResult.toList
    }
    def getResult(row: Int): Map[String, String] ={
        val decimalFormat = new DecimalFormat("###################.##################")
        Map(
            "hosptalCount" -> decimalFormat.format(sheet.getRow(row).getCell(5).getNumericCellValue),
            "productCount" -> decimalFormat.format(sheet.getRow(row).getCell(6).getNumericCellValue),
            "sales" -> sheet.getRow(row).getCell(7).toString,
            "units" -> sheet.getRow(row).getCell(8).toString
        )
    }
}

object ReadExcelFile {
    def apply(xls_file: String) = new ReadExcelFile(xls_file: String)
}
