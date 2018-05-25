package com.pharbers.panel.test

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.specs2.Specification

@RunWith(classOf[JUnitRunner])
class specsTest01 extends Specification{def is = s2"""
  This is a Specification to check the 'Hello world' String

  The 'Hello workd' String should
  contain 11 characters        $e1
  start with "Hello"           $e2
  end with "world"             $e3
                                                    """
  def e1 = "Hello world" must have size(11)
  def e2 = "Hello world" must startWith("Hello")
  def e3 = "Hello world" must endWith("world")
}
