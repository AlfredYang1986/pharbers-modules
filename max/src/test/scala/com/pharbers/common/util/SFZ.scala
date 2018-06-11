package com.pharbers.common.util

import org.scalatest.FunSuite

/**
  * Created by clock on 18-5-11.
  */
class SFZ extends FunSuite{
    test("get SFZ last number") {
        val input = "22020219950727721"
        val coefficientMapping = Array(7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2)
        val resultMapping = Array("1","0","X","9","8","7","6","5","4","3","2")
        val temp = input.toArray.map(_.toInt - 48) zip coefficientMapping
        val sum = temp.map{x => x._1 * x._2}.sum
        val result = resultMapping(sum % 11)
        println(input + result)
    }
}
