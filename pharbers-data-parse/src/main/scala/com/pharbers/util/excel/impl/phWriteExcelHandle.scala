package com.pharbers.util.excel.impl

import java.io.FileOutputStream

import org.apache.poi.xssf.usermodel.{XSSFSheet, XSSFWorkbook}
import org.xml.sax.helpers.DefaultHandler

/**
  * Created by clock on 17-9-7.
  */
class phWriteExcelHandle(output_file: String) extends DefaultHandler {

    def write(content: List[Map[String, String]], sheetName: String = "Sheet1") = {
        val wb = new XSSFWorkbook()
        val sheet = wb.createSheet(sheetName)

        writeSheet(content,sheet)

        val os = new FileOutputStream(output_file)
        wb.write(os)
        os.close()
    }

    def writeSheet(content: List[Map[String, String]], sheet: XSSFSheet) = {
        writeTitle(content.head.keys.toArray, sheet)

        var rowNum = 1
        var cellNum = 0
        content.foreach { r =>
            val row = sheet.createRow(rowNum)
            r.foreach { c =>
                val cell = row.createCell(cellNum)
                cell.setCellValue(c._2)
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
