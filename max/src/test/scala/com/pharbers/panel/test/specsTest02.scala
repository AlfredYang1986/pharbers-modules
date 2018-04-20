package com.pharbers.panel.test

import org.specs2.Specification

class specsTest02 extends Specification {def is = s2"""
  a $e1
                                                     b $e2
  """
  val a: String = "1"
  val b: Int = 1
  val c: String = "a"
  def e1 = a must_=== c
  def e2 = a ==== c
}
