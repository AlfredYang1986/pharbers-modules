package com.pharbers.panel.test

object getResult extends App {
  val companyList = ReadXmlFile().readXmlFile
  companyList.map(x => new ExecuteJob(x))
}
