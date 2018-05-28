//package com.pharbers.unitTest.getResult
//
//import com.pharbers.spark.phSparkDriver
//import com.pharbers.unitTest.readFiles._
//
//case class GetResult() {
//
//    def getResult: List[Map[String, String]] = {
//
//        lazy val jarInfo = ("pharbers-unitTest-0.1.jar", "./target/pharbers-unitTest-0.1.jar")
//        val sc = phSparkDriver().sc
//        if (!sc.listJars().exists(x => x.contains(jarInfo._1)))
//            sc.addJar(jarInfo._2)
//        val companys = readXmlFile().readXmlFile()
//        val resultFiles = companys.map(ExecuteJob(_).getresultPath)
//        resultFiles
//
//
////        val companyParamter = ReadOneXmlFIle().getCompany()
////        val csvFileName = ExecuteJob(companyParamter).getresultPath
////        val areaList = ReadExcelFile().getArea()
////        val csvResult = ReadCSVFile(csvFileName, areaList).getCsvResult()
////        val excelResult = ReadExcelFile().getExcelResult
////        List(csvResult, excelResult)
//    }
//}
