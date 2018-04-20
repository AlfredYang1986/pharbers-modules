package com.pharbers.panel.test

object JobTestClient extends App {
  val director: Director = new Director
  val nhwaJobParameterBuilder: nhwaJobParameterBuilder = new nhwaJobParameterBuilder
  director.Construct(nhwaJobParameterBuilder)
  val jobParameter = nhwaJobParameterBuilder.creatjobParameter()

}
