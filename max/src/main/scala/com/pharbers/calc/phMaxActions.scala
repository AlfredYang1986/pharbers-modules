package com.pharbers.calc

/**
  * Created by jeorch on 18-4-9.
  */
case class phMaxActions(args: Map[String, String]) extends phMaxActionsTrait {

    override val name = ""
    override val uid: String = args.getOrElse("uid", throw new Exception("no find uid arg"))
    override lazy val panel: String = args.getOrElse("panel", throw new Exception("no find panel arg"))

}
