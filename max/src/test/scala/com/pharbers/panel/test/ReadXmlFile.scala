package com.pharbers.panel.test

import com.pharbers.panel.test.Builder.{CompanyParamter, CompanyParamterFactory}

import scala.xml._

/**
  * 读取xml文件
  */
class ReadXmlFile {
  def readXmlFile ={
    val xmlFile = XML.loadFile("/home/cui/download/pharbers-modules/max/src/test/scala/com/pharbers/panel/test/test.xml")
    var companyList: List[CompanyParamter] = Nil
    xmlFile match {
      case  <tests>{allTest @ _*}</tests> =>
        for(testNode @ <test>{_*}</test> <- allTest){
          var company = testNode\"company"\"@value"
          company.toString() match {
            case "nhwa" => companyList = CompanyParamterFactory().nhwaParamterBuild(
              (testNode\"company"\"@value").toString(),
              (testNode\"data"\"@value").toString(),
              (testNode\"market"\"@value").toString(),
              (testNode\"cpa_file"\"@value").toString())::companyList
            case "asttlas" => companyList = CompanyParamterFactory().asttlasParamterBuild(
              (testNode\"company"\"@value").toString(),
              (testNode\"data"\"@value").toString(),
              (testNode\"market"\"@value").toString(),
              (testNode\"cpa_file"\"@value").toString(),
              (testNode\"gycx_file"\"@value").toString()
            )::companyList
            case _ => throw new IllegalArgumentException("请输入正确的公司名")
          }
        }
      case _ => throw new IllegalArgumentException("xml结构不正确")
    }
    companyList  
  }
}

object ReadXmlFile{
  def apply(): ReadXmlFile = new ReadXmlFile()
}
