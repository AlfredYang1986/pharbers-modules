package com.pharbers.panel.pfizer

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.DataFrame

/**
  * Created by jeorch on 18-4-28.
  */
trait phPfizerPanelCommonTrait {

    def getUnionRDD[T](lst_path: List[String])(func: String => RDD[T]): RDD[T] = lst_path match {
        case Nil => throw new Exception("no universe file found")
        case head::Nil => func(head)
        case head::tail => func(head).union(getUnionRDD[T](tail)(func))
    }

    def getUnionDF(lst_path: List[String])(func: String => DataFrame): DataFrame = lst_path match {
        case Nil => throw new Exception("no universe file found")
        case head::Nil => func(head)
        case head::tail => func(head).union(getUnionDF(tail)(func))
    }

    /**
      * 之后如果出现新的拆分市场,只需维护getChildMarkets函数即可
      * @param curr_mkt
      * @return
      */

    def getChildMarkets(curr_mkt: String): List[String] = curr_mkt match {
        case "AI_R_other" => "AI_D"::"ZYVOX"::Nil
        case "PAIN_other" => "PAIN_C"::Nil
        case "HTN" => "HTN2"::Nil
        case "AI_W" => "AI_D"::Nil
        case _ => throw new Exception(s"Error in phPfizerPanelSplitFatherMarketAction. Market=${curr_mkt} has no child market!")
    }

}
