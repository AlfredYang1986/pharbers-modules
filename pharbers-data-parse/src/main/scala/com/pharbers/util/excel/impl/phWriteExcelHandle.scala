package com.pharbers.util.excel.impl

import java.io.FileOutputStream

import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.xssf.streaming.SXSSFWorkbook
import org.xml.sax.helpers.DefaultHandler

/**
  * Created by clock on 17-9-7.
  */
class phWriteExcelHandle(output_file: String) extends DefaultHandler {

    def write(content: List[Map[String, Any]], sheetName: String = "Sheet1")(implicit cellNumMap: Map[String, Int]): Boolean = {
        val wb = new SXSSFWorkbook(1000)//缓存
        wb.setCompressTempFiles(true)
        val sheet = wb.createSheet(sheetName)
        writeSheet(content,sheet)
        val os = new FileOutputStream(output_file)
        wb.write(os)
        os.flush()
        os.close()
        true
    }

    def writeSheet(content: List[Map[String, Any]], sheet: Sheet)(implicit cellNumMap: Map[String, Int]) = {
        if(content == Nil)
            throw new Exception("写入的数据为空")
        writeTitle(content.head.keys.toList, sheet)
        for(i <- 1 to content.length){
            val rowRef = sheet.createRow(i)
            content(i-1).foreach { c =>
                val cell = rowRef.createCell(cellNumMap(c._1))
                c._2 match {
                    case i:Int => cell.setCellValue(i)
                    case l:Long => cell.setCellValue(l)
                    case f:Float => cell.setCellValue(f)
                    case d:Double => cell.setCellValue(d)
                    case s: String => cell.setCellValue(s)
                    case _ => cell.setCellValue(c._2.asInstanceOf[String])
                }
            }
        }

    }

    def writeTitle(title: List[String], sheet: Sheet)(implicit cellNumMap: Map[String, Int]) = {
        val row = sheet.createRow(0)
        if(title.diff(cellNumMap.keys.toList) != Nil)
            throw new Exception("写入Excel时，列关系对应异常")
        cellNumMap.foreach{x =>
            row.createCell(x._2).setCellValue(x._1)
        }
    }
}
