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

    def process(sheetId: Int, sheetName: String = "") = {
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

    private var ref: String = ""
    private var lastContents = ""

    private var phNextIsNull = false
    private var nextIsTitle = true
    private var phNextIsString = false
    private var phNextIsNumber = false


    override def startElement(uri: String, localName: String, name: String, attr: Attributes) = {
        if ("inlineStr".equals(name) || "v".equals(name)) {
            phIsOpen = true
        }else if (name.equals("c")) {// c => 单元格
            // 如果下一个元素是 SST 的索引，则将nextIsString标记为true
            val cellType = attr.getValue("t")

            //单元格是否为空
            if (cellType == null) {
                phNextIsNull = true
            } else {
                phNextIsNull = false
            }

            if(cellType != null && "s".equals(cellType)) phNextIsString = true
            else phNextIsString = false

            //改变当前指向单元格
            ref = attr.getValue("r")
        }
        // 置空
        lastContents = ""
    }

    private var resultList:List[Map[String,String]] = Nil
    protected var titleMap: Map[String,String] = Map()
    protected var rowMap: Map[String,String] = Map()


    override def endElement(uri: String, localName: String, name: String) = {
        // 根据SST的索引值的到单元格的真正要存储的字符串
        // 这时characters()方法可能会被调用多次
        if (phNextIsString && name == "v") {
            try {
                val idx = Integer.parseInt(lastContents)
                lastContents = new XSSFRichTextString(sst.getEntryAt(idx)).toString
                if(lastContents.startsWith("0") && !lastContents.startsWith("0."))
                    lastContents = lastContents.tail
            } catch {
                case _: Exception => Unit
            }
        }

        if(phNextIsNull && lastContents != "") phNextIsNumber = true

        name match {
            case "v" if nextIsTitle => titleMap += getPrefix -> lastContents.trim
            case "v" => rowMap += getPrefix -> lastContents.trim
            case "c" if phNextIsNumber => rowMap += getPrefix -> lastContents.trim
            case "c" if phNextIsNull => rowMap += getPrefix -> ""
            case "c" => Unit
            case "row" if nextIsTitle => {// 如果标签名称为 row ，这说明已到行尾
                nextIsTitle = false
                titleMap = replaceFiledFun(titleMap)
                ref = null
            }
            case "row" if rowMap != Nil => {
                if(titleMap.size - rowMap.size > 0)
                    completionRowMap
                processFun() match {
                    case Some(map) => resultList = resultList :+ map
                    case None => Unit
                }
                rowMap = Map()
                ref = null
            }
            case _ => Unit
        }
    }

    override def characters(ch: Array[Char], start: Int, length: Int) = {
        if(phIsOpen) lastContents += new String(ch, start, length)
    }

    protected def processFun(): Option[Map[String,String]] = {
        Some(titleMap.keys.map{x => (titleMap(x),rowMap(x))}.toMap)
    }

    protected val fieldMap: Map[String, String] = Map()
    private def replaceFiledFun(old: Map[String,String]): Map[String, String] = {
        old.map{ x => (x._1, fieldMap.getOrElse(x._2,x._2)) }
    }

    protected val defaultValueMap: Map[String, String] = Map()
    protected def getDefaultValue(cell: String, tr: Map[String,String]): String = {
        def getValue(targetCell: String): String = {
            tr.get(targetCell) match {
                case Some(s) if s == "" => getDefaultValue(targetCell, tr)
                case Some(s) => s
                case None => ""
            }
        }
        defaultValueMap.get(cell) match {
            case Some(s) if s.startsWith("$") => getValue(s.tail)
            case Some(s) => s
            case None => ""
        }
    }

    private def completionRowMap = {
        titleMap.keys.foreach{t =>
            rowMap.get(t) match {
                case Some(s) => Unit
                case None => rowMap += t -> ""
            }
        }
    }

    private def getPrefix = ref.split("\\d").head
}
