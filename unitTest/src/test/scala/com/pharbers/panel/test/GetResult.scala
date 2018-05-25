//package com.pharbers.panel.test
//
//import com.pharbers.panel.test.readExcelFile.ReadExcelFile
//
//class GetResult {
//  def getResult: List[List[Map[String, Any]]] ={
//    val companyList = ReadXmlFile().readXmlFile
//    val resultList: List[String] = ExecuteJob(companyList).getresultPath
//    val csvResult: List[Map[String, Any]] = resultList.map(r => ReadCSVFile().getcsvResult(r))
//    val excelResult: List[Map[String, Any]] = ReadExcelFile()
//    List(csvResult, excelResult)
//  }
//}
//
//object GetResult{
//  def apply(): GetResult = new GetResult()
//}
