package com.pharbers.calc.actions

import java.util.UUID

import com.pharbers.builder.phMarketTable.phMarketManager
import com.pharbers.common.algorithm.max_path_obj
import com.pharbers.pactions.actionbase._
import org.apache.spark.sql.functions.{col, _}

object phMaxPersistentAction {
    def apply[T](args: pActionArgs = NULLArgs): pActionTrait = new phMaxPersistentAction[T](args)
}

class phMaxPersistentAction[T](override val defaultArgs: pActionArgs) extends pActionTrait with phMarketManager{
    override val name: String = "max_persistent_action"

    override def perform(prMap: pActionArgs): pActionArgs = {

        val company = defaultArgs.asInstanceOf[MapArgs].get("company").asInstanceOf[StringArgs].get
        val condition = getAllSubsidiary(company).map(x => col("Product") like s"%$x%").reduce((a, b) => a or b) //获得所有子公司
        val maxDF = prMap.asInstanceOf[MapArgs].get("max_calc_action").asInstanceOf[DFArgs].get
        val max_result = maxDF.withColumn("belong2company", when(condition, 1).otherwise(0))
        val panelName = defaultArgs.asInstanceOf[MapArgs].get("name").asInstanceOf[StringArgs].get
        val maxName = panelName + UUID.randomUUID().toString
        val resultLocation = max_path_obj.p_maxPath + maxName

        max_result.write
                .format("csv")
                .option("header", value = true)
                .option("delimiter", 31.toChar.toString)
                .option("codec", "org.apache.hadoop.io.compress.GzipCodec")
                .save(resultLocation)

        StringArgs(maxName)
    }
}