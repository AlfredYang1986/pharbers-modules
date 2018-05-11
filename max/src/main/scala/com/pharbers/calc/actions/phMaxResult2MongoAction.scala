package com.pharbers.calc.actions

import com.pharbers.delivery.util.ConfigMongo
import com.pharbers.pactions.actionbase._

object phMaxResult2MongoAction {
    def apply(args: pActionArgs = NULLArgs): pActionTrait = new phMaxResult2MongoAction(args)
}

class phMaxResult2MongoAction(override val defaultArgs: pActionArgs) extends pActionTrait  with ConfigMongo  {
    override val name: String = "phMaxResult2MongoAction"
    override def perform(pr : pActionArgs): pActionArgs = {
        val max_result = pr.asInstanceOf[MapArgs].get("max_calc_action").asInstanceOf[DFArgs].get
        val singleJobKey = pr.asInstanceOf[MapArgs].get("phMaxInfo2RedisAction").asInstanceOf[StringArgs].get

        max_result.write.format("com.mongodb.spark.sql.DefaultSource").mode("overwrite")
            .option("uri", s"mongodb://$mongodbHost:$mongodbPort/")
            .option("database", "Max_Test")
            .option("collection", singleJobKey)
            .save()
        NULLArgs
    }
}