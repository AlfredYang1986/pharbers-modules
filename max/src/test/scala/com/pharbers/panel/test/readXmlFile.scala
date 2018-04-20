package com.pharbers.panel.test

import scala.xml._

/**
  * 读取xml文件
  */
object readXmlFile{
  def getXmlParameter: Map[String, String] ={
    val xmlFile = XML.loadFile("/home/cui/download/pharbers-modules/max/src/test/scala/com/pharbers/panel/test/test.xml")
    val company = xmlFile\"tests"\"test"\"company"\"@value"
    val data = xmlFile\"tests"\"test"\"Data"\"@value"
    val market = xmlFile\"tests"\"test"\"market"\"@value"
    val inputFile = xmlFile\"tests"\"test"\"inputFile"\"@value"

    val parameter = Map("company" -> company.toString(), "data" -> data.toString(), "market" -> market.toString(), "inputFile" -> inputFile.toString())
    parameter
  }
}
