package com.pharbers.panel.test.readExcelFile

import java.io.FileInputStream
import org.apache.poi.xssf.usermodel.XSSFWorkbook

class ReadExcelFile(xls_file: String, rows: Int){
  val ins = new FileInputStream(xls_file)
  val xssfWorkbook = new XSSFWorkbook(ins)
  val xssfSheet = xssfWorkbook.getSheetAt(1)
  def readExcel={
    val excelResult: List[Map[String, Any]] = Range(1,rows+1).map(row => getCellValue(row)).toList
    excelResult
  }
  def getCellValue(row: Int): Map[String, Any] ={
    val hosptalsum = xssfSheet.getRow(row).getCell(3).getNumericCellValue
    val sales = xssfSheet.getRow(row).getCell(5).getNumericCellValue
    val units = xssfSheet.getRow(row).getCell(6).getNumericCellValue
    Map("hosptalsum" -> hosptalsum, "sales" -> sales, "units" -> units)
  }
}

object ReadExcelFile{
  def apply(xls_file: String, rows: Int): ReadExcelFile = new ReadExcelFile(xls_file, rows)
}
