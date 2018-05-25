package com.pharbers.unitTest.readFiles

import java.io.FileInputStream
import java.text.DecimalFormat

import org.apache.poi.xssf.usermodel.XSSFWorkbook
import scala.math.BigDecimal

class ReadExcelTemp(xls_file: String) {
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
}

object ReadExcelTemp {
    def apply(xls_file: String) = new ReadExcelFileTemp(xls_file: String)
}
