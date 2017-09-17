package com.pharbers.util.excel.impl

import java.io.FileOutputStream

import org.apache.poi.xssf.usermodel.{XSSFSheet, XSSFWorkbook}
import org.xml.sax.helpers.DefaultHandler

/**
  * Created by clock on 17-9-7.
  */
class phWriteExcelHandle(output_file: String) extends DefaultHandler {

    def write(content: List[Map[String, Any]], sheetName: String = "Sheet1"): List[Map[String, Any]] = {
        val wb = new XSSFWorkbook()
        val sheet = wb.createSheet(sheetName)

        writeSheet(content,sheet)

        val os = new FileOutputStream(output_file)
        wb.write(os)
        os.close()
        content
    }

    def writeSheet(content: List[Map[String, Any]], sheet: XSSFSheet) = {
        writeTitle(content.head.keys.toArray, sheet)

        var rowNum = 1
        var cellNum = 0
        content.foreach { row =>
            val rowRef = sheet.createRow(rowNum)
            row.foreach { c =>
                val cell = rowRef.createCell(cellNum)
                val a = new String
                c._2 match {
                    case i:Int => cell.setCellValue(i)
                    case l:Long => cell.setCellValue(l)
                    case f:Float => cell.setCellValue(f)
                    case d:Double => cell.setCellValue(d)
                    case s: String => cell.setCellValue(s)
                    case _ => cell.setCellValue(c._2.asInstanceOf[String])
                }
                cellNum += 1
            }
            rowNum += 1
            cellNum = 0
        }
    }

    def writeTitle(title: Array[String], sheet: XSSFSheet) = {
        val row = sheet.createRow(0)
        for(i <- title.indices) yield {
            val cell = row.createCell(i)
            cell.setCellValue(title(i))
        }
    }
}
