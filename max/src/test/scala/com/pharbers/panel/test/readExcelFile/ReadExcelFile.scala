package com.pharbers.panel.test.readExcelFile

import java.io.FileInputStream
import org.apache.poi.xssf.usermodel.XSSFWorkbook


class ReadExcelFile{
  val xls_file: String = "/home/cui/download/验收结果比对表 (复件).xlsx"
  val ins = new FileInputStream(xls_file)
  val xssfWorkbook = new XSSFWorkbook(ins)
  val sheet = xssfWorkbook.getSheetAt(1)
  val firstRowNumber = sheet.getFirstRowNum
  val lastRowNumber = sheet.getLastRowNum
  val hosptalnum = sheet.getRow(firstRowNumber).getCell(3).toString
  val productnum = sheet.getRow(firstRowNumber).getCell(4).toString
  val sales = sheet.getRow(firstRowNumber).getCell(5).toString
  val units = sheet.getRow(firstRowNumber).getCell(6).toString
  val excelResultList:List[Map[String, String]] = Range(1,lastRowNumber+1).map(row => getRows(row)).toList
  def getRows(row: Int): Map[String, String] ={
    Map(hosptalnum -> sheet.getRow(row).getCell(3).toString,
      productnum -> sheet.getRow(row).getCell(4).toString,
      sales -> sheet.getRow(row).getCell(5).toString,
      units -> sheet.getRow(row).getCell(6).toString
    )
  }
}

object ReadExcelFile{
  def apply(): List[Map[String,String]] = new ReadExcelFile().excelResultList
}