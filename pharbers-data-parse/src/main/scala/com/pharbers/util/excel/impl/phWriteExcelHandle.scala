package com.pharbers.util.excel.impl

import java.io.FileOutputStream

import org.apache.poi.xssf.usermodel.{XSSFSheet, XSSFWorkbook}
import org.xml.sax.helpers.DefaultHandler

/**
  * Created by clock on 17-9-7.
  */
class phWriteExcelHandle(output_file: String) extends DefaultHandler {

    def write(content: List[Map[String, Any]], sheetName: String = "Sheet1")(implicit cellNumMap: Map[String, Int]): List[Map[String, Any]] = {
        val wb = new XSSFWorkbook()
        val sheet = wb.createSheet(sheetName)

        writeSheet(content,sheet)

        val os = new FileOutputStream(output_file)
        wb.write(os)
        os.close()
        Nil
    }

    def writeSheet(content: List[Map[String, Any]], sheet: XSSFSheet)(implicit cellNumMap: Map[String, Int]) = {
        writeTitle(content.head.keys.toList, sheet)

        var rowNum = 1
        content.foreach { row =>
            val rowRef = sheet.createRow(rowNum)
            row.foreach { c =>
                val cell = rowRef.createCell(cellNumMap(c._1))
                val a = new String
                c._2 match {
                    case i:Int => cell.setCellValue(i)
                    case l:Long => cell.setCellValue(l)
                    case f:Float => cell.setCellValue(f)
                    case d:Double => cell.setCellValue(d)
                    case s: String => cell.setCellValue(s)
                    case _ => cell.setCellValue(c._2.asInstanceOf[String])
                }
            }
            rowNum += 1
        }
    }

    def writeTitle(title: List[String], sheet: XSSFSheet)(implicit cellNumMap: Map[String, Int]) = {
        val row = sheet.createRow(0)
        if(title.diff(cellNumMap.keys.toList) != Nil)
            throw new Exception("写入Excel时，列关系对应异常")
        cellNumMap.foreach{x =>
            val cell = row.createCell(x._2)
            cell.setCellValue(x._1)
        }
    }
}
