package com.pharbers.unitTest.readFiles

import com.pharbers.unitTest.builder.{CompanyParamter, CompanyParamterFactory}
import com.pharbers.unitTest.newBuilder.Company

import scala.xml.XML

class readXmlFile() {
  val xmlFile = XML.loadFile("/home/cui/download/unitTest/src/test/scala/com/pharbers/panel/test/test.xml")
  def readXmlFile(): List[CompanyParamter] ={
    var companyList: List[CompanyParamter] = Nil
    xmlFile match {
      case  <tests>{allTest @ _*}</tests> =>
        for(testNode @ <test>{_*}</test> <- allTest){
          val company = testNode\"company"\"@value"
          company.toString() match {
            case "nhwa" => companyList = CompanyParamterFactory().nhwaParamterBuild(
              (testNode\"company"\"@value").toString(),
              (testNode\"data"\"@value").toString(),
              (testNode\"market"\"@value").toString(),
              (testNode\"cpa_file"\"@value").toString(),
              (testNode\"resultMatch_file"\"@value").toString()
            )::companyList
            case "asttlas" => companyList = CompanyParamterFactory().asttlasParamterBuild(
              (testNode\"company"\"@value").toString(),
              (testNode\"data"\"@value").toString(),
              (testNode\"market"\"@value").toString(),
              (testNode\"cpa_file"\"@value").toString(),
              (testNode\"gycx_file"\"@value").toString(),
              (testNode\"resultMatch_file"\"@value").toString()
            )::companyList
            case "pfizer" => companyList = CompanyParamterFactory().pfizerParamterBuild(
              (testNode\"company"\"@value").toString(),
              (testNode\"data"\"@value").toString(),
              (testNode\"market"\"@value").toString(),
              (testNode\"cpa_file"\"@value").toString(),
              (testNode\"gycx_file"\"@value").toString(),
              (testNode\"resultMatch_file"\"@value").toString()
            )::companyList
            case _ => throw new IllegalArgumentException("请输入正确的公司名")
          }
        }
      case _ => throw new IllegalArgumentException("xml结构不正确")
    }
    companyList
  }
}

object readXmlFile{
  def apply(): readXmlFile = new readXmlFile()
}

