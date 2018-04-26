package com.pharbers.panel.test.readExcelFile

import java.io.FileInputStream
import org.apache.poi.xssf.usermodel.XSSFWorkbook

object ReadExcelFile{
  def readExcel(xls_file: String) ={
    val ins = new FileInputStream(xls_file)
    val xssfWorkbook = new XSSFWorkbook(ins)
    val xssfSheet = xssfWorkbook.getSheetAt(1)
    val hosptalCount = xssfSheet.getRow(1).getCell(3).getNumericCellValue
    val sales = xssfSheet.getRow(1).getCell(5).getNumericCellValue
    val units = xssfSheet.getRow(1).getCell(6).getNumericCellValue
    List(hosptalCount, sales, units)
  }
}
