package com.pharbers.pactions.generalactions.memory

import org.apache.spark.sql.DataFrame

object PhMemory {
//    var memory  : Map[String, RDD[_]] = Map.empty
    var memory  : Map[String, DataFrame] = Map.empty

//    def registerMemory(s : String, r : RDD[_]) = {
    def registerMemory(s : String, r : DataFrame) = {
        r.persist()
        memory = memory + (s -> r)
    }

//    def queryMemory(s : String) : RDD[_] = memory.get(s).get
    def queryMemory(s : String) : DataFrame = memory.get(s).get
//    def queryMemoryWithType[T](s : String) : RDD[T] = memory.get(s).get.asInstanceOf[RDD[T]]

    def clean(s : String) = queryMemory(s).unpersist()
    def clean = memory foreach(x => x._2.unpersist())

    def isExist(s : String) : Boolean = memory.contains(s)
}
