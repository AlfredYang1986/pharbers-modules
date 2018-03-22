package com.pharbers.sparkSteam.paction.actionbase

import org.apache.spark.rdd.RDD

trait pActionArgs {
    type t
    def get : t
}

case class RDDArgs[T](rdd : RDD[T]) extends pActionArgs {
    type t = RDD[T]
    override def get : RDD[T] = rdd
}

case class StringArgs(str : String) extends pActionArgs {
    type t = String
    override def get : String = str
}

case class ListArgs(lst : List[pActionArgs]) extends pActionArgs {
    type t = List[pActionArgs]
    override def get : List[pActionArgs] = lst
}

case class MapArgs(map : Map[String, pActionArgs]) extends pActionArgs {
    type t = Map[String, pActionArgs]
    override def get : Map[String, pActionArgs] = map
}

object NULLArgs extends pActionArgs {
    type t = Unit
    override def get : Unit  = ???
}
