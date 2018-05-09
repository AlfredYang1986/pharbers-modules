package com.pharbers.pactions.generalactions

import com.pharbers.pactions.actionbase.{NULLArgs, pActionArgs, pActionTrait}
import com.pharbers.spark.phSparkDriver

object jarPreloadAction {
    def apply(arg_name: String = "jarPreloadJob") : pActionTrait =
        new jarPreloadAction(arg_name)
}

class jarPreloadAction(override val name: String) extends pActionTrait {

    lazy val lst: List[(String, String)] =  ("commons-codec-1.9.jar", "./jar/commons-codec-1.9.jar") ::
                    ("dom4j-1.1.jar", "./jar/dom4j-1.1.jar") ::
                    ("xmlbeans-2.3.0.jar", "./jar/xmlbeans-2.3.0.jar") ::
                    ("poi-3.13.jar", "./jar/poi-3.13.jar") ::
                    ("poi-ooxml-3.13.jar", "./jar/poi-ooxml-3.13.jar") ::
                    ("poi-ooxml-schemas-3.13.jar", "./jar/poi-ooxml-schemas-3.13.jar") ::
                    ("xlsx-streamer-1.0.2.jar", "./jar/xlsx-streamer-1.0.2.jar") ::
                    ("pharbers-max-0.1.jar", "./jar/pharbers-max-0.1.jar") :: Nil

    override def perform(args : pActionArgs)(implicit f: (Double, String) => Unit) : pActionArgs = {
        val sc = phSparkDriver().sc
        lst.foreach { iter =>
            if (!sc.listJars().exists(x => x.contains(iter._1))) sc.addJar(iter._2)
        }

        NULLArgs
    }

    override val defaultArgs : pActionArgs = NULLArgs
    override val progressFactor: Int = 0
    override implicit def progressFunc(progress : Double, flag : String) : Unit = {}
}