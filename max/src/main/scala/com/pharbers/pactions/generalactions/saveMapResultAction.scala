package com.pharbers.pactions.generalactions

import com.pharbers.pactions.actionbase._

/**
  * Created by spark on 18-3-30.
  */
object saveMapResultAction {
    def apply[T](arg_key: String, arg_path: String): pActionTrait =
        new saveMapResultAction[T](arg_key, arg_path)
}

class saveMapResultAction[T](key: String, path: String) extends pActionTrait {

    override val name: String = "saveMapResultAction"
    override val defaultArgs: pActionArgs = NULLArgs
    override implicit def progressFunc(progress: Double, flag: String): Unit = {}

    override def perform(prMap: pActionArgs)(implicit f: (Double, String) => Unit): pActionArgs = {

        val rdd = prMap.asInstanceOf[MapArgs].get.get(key) match {
            case Some(r) => r.asInstanceOf[RDDArgs[T]].get
            case None => throw new Exception(s"not found key=$key in saveMapResultAction.class")
        }

        rdd.coalesce(1).saveAsTextFile(path)
        prMap
    }

}
