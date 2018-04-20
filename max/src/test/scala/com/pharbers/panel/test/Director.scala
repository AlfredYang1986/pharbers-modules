package com.pharbers.panel.test

class Director {
  def Construct(jobParameterBuilder: JobParameterBuilder): Unit ={
    jobParameterBuilder.buildCompany()
    jobParameterBuilder.buildData()
    jobParameterBuilder.buildInputFile()
    jobParameterBuilder.buildMarket()
  }
}
