package com.pharbers.panel.test

abstract class JobParameterBuilder {
  def buildCompany(): Unit
  def buildMarket(): Unit
  def buildInputFile(): Unit
  def buildData(): Unit
  def creatjobParameter(): JobParameter
}
