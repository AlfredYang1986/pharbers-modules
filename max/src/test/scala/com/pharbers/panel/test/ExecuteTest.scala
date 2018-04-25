package com.pharbers.panel.test

import com.pharbers.panel.test.Builder.CompanyParamterFactory
import com.pharbers.panel.test.readExcelFile.ReadExcelFile

class ExecuteTest{
  val parameter = readXmlFile.getXmlParameter
  def getMaxResult ={
    val companyParamterFactory = new CompanyParamterFactory
    val nhwaParamter = companyParamterFactory.NhwaParamterBuild(parameter)
    val executeJob = new ExecuteJob(nhwaParamter)
    val maxResult = ReadCSVFile.getMaxResult
    maxResult
  }
  def getexcelResult={
    val excelResult = ReadExcelFile.readExcel(parameter("matchFile"))
    excelResult
  }
}
