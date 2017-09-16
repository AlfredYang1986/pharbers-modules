package com.pharbers.util.excel.impl

import java.io.InputStream

import org.apache.poi.openxml4j.opc.OPCPackage
import org.apache.poi.xssf.eventusermodel.XSSFReader
import org.apache.poi.xssf.eventusermodel.XSSFReader.SheetIterator
import org.apache.poi.xssf.model.SharedStringsTable
import org.apache.poi.xssf.usermodel.XSSFRichTextString
import org.xml.sax.{Attributes, InputSource, XMLReader}
import org.xml.sax.helpers.{DefaultHandler, XMLReaderFactory}

/**
  * Created by clock on 17-9-7.
  */
class phReadExcelHandle(file_local: String) extends DefaultHandler {
    private val pkg = OPCPackage.open(file_local)
    private val fr = file_local match {
        case s:String if s.endsWith(".xlsx") => new XSSFReader(pkg)
        case _ => throw new Exception("file type error")
    }
    private val sst: SharedStringsTable = fr.getSharedStringsTable

    lazy val getCount: Int = {
        val xs = fr.getSheetsData.asInstanceOf[XSSFReader.SheetIterator]

        var count = 0
        while(xs.hasNext){
            count += 1
            xs.next()
        }
        count
    }

    def process(sheetId: Int = 1,
                sheetName: String = "") = {
        val parser: XMLReader = fetchSheetParser(sst)

        val index = if (sheetName != "") {
            //根据sheet名字读取
            getSheetIndex(fr.getSheetsData.asInstanceOf[XSSFReader.SheetIterator],sheetName)
        } else { sheetId }

        //读取指定sheet
        val sheet:InputStream  = fr.getSheet("rId" + index)
        val sheetSource = new InputSource(sheet)
        parser.parse(sheetSource)
        sheet.close()

        resultList
    }

    private def getSheetIndex(xs: SheetIterator, sheetName: String): Int = {
        var index = 0
        var flag = 0
        while(xs.hasNext && flag == 0){
            xs.next()
            if(xs.getSheetName == sheetName) flag = 1
            else index += 1
        }

        if(index == 0)
            throw new Exception("parse xlsx error => not found sheet name")
        else
            index += 1

        index
    }

    private def fetchSheetParser(sst: SharedStringsTable) = {
        val parser = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser")
        parser.setContentHandler(this)
        parser
    }

    private var phIsOpen = false
    private var phNextNull = false
    private var preRef: String = _
    private var ref: String = _
    private var nextIsString = false
    private var dateFlag = false
    private var numberFlag = false
    private var lastContents = ""

    override def startElement(uri: String, localName: String, name: String, attr: Attributes) = {
        if ("inlineStr".equals(name) || "v".equals(name)) {
            phIsOpen = true
        }else if (name.equals("c")) {// c => 单元格
            // 如果下一个元素是 SST 的索引，则将nextIsString标记为true
            val cellType = attr.getValue("t")

            //单元格是否为空
            if (cellType == null) {
                phNextNull = true
            } else {
                phNextNull = false
            }

            if (cellType != null && cellType.equals("s")) {
                nextIsString = true
            } else {
                nextIsString = false
            }

            // 日期格式
            val cellDateType = attr.getValue("s")
            if (cellDateType != null && cellDateType.equals("1")) {
                dateFlag = true
            } else {
                dateFlag = false
            }

            //数字格式
            val cellNumberType = attr.getValue("s")
            if (cellNumberType != null && cellNumberType.equals("2")) {
                numberFlag = true
            } else {
                numberFlag = false
            }

            // 前一个单元格的位置
            if (preRef == null) {
                preRef = attr.getValue("r")
            } else {
                preRef = ref
            }

            //改变当前指向单元格
            ref = attr.getValue("r")
        }
        // 置空
        lastContents = ""
    }

    protected var rowList: List[String] = Nil
    private var nextIsTitle = true
    protected var titleList: List[String] = Nil
    private var resultList:List[Map[String,String]] = Nil

    override def endElement(uri: String, localName: String, name: String) = {
        // 根据SST的索引值的到单元格的真正要存储的字符串
        // 这时characters()方法可能会被调用多次
        if (nextIsString) {
            try {
                val idx = Integer.parseInt(lastContents)
                lastContents = new XSSFRichTextString(sst.getEntryAt(idx)).toString
            } catch {
                case _: Exception => Unit
            }
        }

        name match {
            case "v" if nextIsTitle => titleList = replaceFiledFun(titleList :+ lastContents.trim)
            case "v" => rowList = rowList :+ lastContents.trim
            case "c" if phNextNull  => rowList = rowList :+ " "
            case "c"  =>  Unit
            case "row" if nextIsTitle => {// 如果标签名称为 row ，这说明已到行尾
                nextIsTitle = false
                preRef = null
                ref = null
            }
            case "row" if rowList != Nil => {// 如果标签名称为 row ，这说明已到行尾
                processFun() match {
                    case Some(map) => resultList = resultList :+ map
                    case None => Unit
                }
                rowList = Nil
                preRef = null
                ref = null
            }
            case _ => Unit
        }
    }

    override def characters(ch: Array[Char], start: Int, length: Int) = {
        if(phIsOpen)
            lastContents += new String(ch, start, length)
    }

    protected def processFun(): Option[Map[String,String]] = {
        Some(titleList.zipAll(rowList, "", "").toMap)
    }

    protected val fieldMap: Map[String, String] = Map()
    private def replaceFiledFun(old: List[String]): List[String] = {
        old.map{x=>
            fieldMap.getOrElse(x,x)
        }
    }

    protected val defaultValueMap: Map[String, String] = Map()
    protected def setDefaultValue(cell: String, tr: Map[String,String]): String = {
        def getValue(targetCell: String): String = {
            tr.get(targetCell) match {
                case None => ""
                case Some(s) => s
            }
        }
        defaultValueMap.get(cell) match {
            case Some(s) if s.startsWith("$") => getValue(s.tail)
            case Some(s) => s
            case None => ""
        }
    }

    /**
    private def getValue(col: Cell): String = {
        col.getCellTypeEnum match {
            case CellType.STRING => col.getRichStringCellValue.getString
            case CellType.NUMERIC if DateUtil.isCellDateFormatted(col) => col.getDateCellValue.toString
            case CellType.NUMERIC => col.getNumericCellValue.toString
            case CellType.BOOLEAN => col.getBooleanCellValue.toString
            case CellType.FORMULA => col.getCellFormula
            case CellType.BLANK => ""
            case _ => throw new Exception("parse xlsx error => cell value error")
        }
    }
    **/
}
