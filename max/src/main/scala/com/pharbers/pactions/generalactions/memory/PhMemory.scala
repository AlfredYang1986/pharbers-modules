package com.pharbers.pactions.generalactions.memory

import org.apache.spark.sql.DataFrame

object PhMemory {

    var memory  : Map[String, DataFrame] = Map.empty

    def registerMemory(s : String, r : DataFrame): Unit = {
        r.persist()
        memory = memory + (s -> r)
    }

    def queryMemory(s : String) : DataFrame = memory.get(s) match {
        case Some(df) => df
        case None => throw new Exception(s"not found key=$s in PhMemory.memory")
    }

    def clean(s : String): DataFrame = queryMemory(s).unpersist()
    def clean(): Unit = memory foreach(x => x._2.unpersist())

    def isExist(s : String) : Boolean = memory.contains(s)
}