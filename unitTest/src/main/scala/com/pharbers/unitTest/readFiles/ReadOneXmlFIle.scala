//package com.pharbers.unitTest.readFiles
//
//import com.pharbers.unitTest.builder.{CompanyParamter, CompanyParamterFactory}
//
//import scala.xml.XML
//
//class ReadOneXmlFIle {
//    val xmlFile = XML.loadFile("/home/cui/download/pharbers-modules/max/src/test/scala/com/pharbers/panel/test/test.xml")
//    val testNode = xmlFile\"test"
//    val company = (testNode\"company"\"@value").toString()
//    def getCompany(): CompanyParamter ={
//        val companyParamter = company match {
//            case "nhwa" => CompanyParamterFactory().nhwaParamterBuild(
//                (testNode\"company"\"@value").toString(),
//                (testNode\"data"\"@value").toString(),
//                (testNode\"market"\"@value").toString(),
//                (testNode\"cpa_file"\"@value").toString())
//            case "asttlas" => CompanyParamterFactory().asttlasParamterBuild(
//                (testNode\"company"\"@value").toString(),
//                (testNode\"data"\"@value").toString(),
//                (testNode\"market"\"@value").toString(),
//                (testNode\"cpa_file"\"@value").toString(),
//                (testNode\"gycx_file"\"@value").toString()
//            )
//        }
//        companyParamter
//    }
//}
//
//object ReadOneXmlFIle{
//    def apply(): ReadOneXmlFIle = new ReadOneXmlFIle()
//}