package com.pharbers.pactions.generalactions

import java.nio.file.{Files, Paths}

import com.pharbers.spark.phSparkDriver
import com.pharbers.pactions.actionbase.{NULLArgs, pActionArgs, pActionTrait}

object jarPreloadAction {
    def apply(arg_name: String = "jarPreloadJob"): pActionTrait =
        new jarPreloadAction(arg_name)
}

class jarPreloadAction(override val name: String) extends pActionTrait {
    override val defaultArgs : pActionArgs = NULLArgs

    lazy val lst: List[(String, String)] =  ("commons-codec-1.9.jar", "./jar/commons-codec-1.9.jar") ::
                    ("dom4j-1.1.jar", "./jar/dom4j-1.1.jar") ::
                    ("xmlbeans-2.3.0.jar", "./jar/xmlbeans-2.3.0.jar") ::
                    ("poi-3.13.jar", "./jar/poi-3.13.jar") ::
                    ("poi-ooxml-3.13.jar", "./jar/poi-ooxml-3.13.jar") ::
                    ("poi-ooxml-schemas-3.13.jar", "./jar/poi-ooxml-schemas-3.13.jar") ::
                    ("xlsx-streamer-1.0.2.jar", "./jar/xlsx-streamer-1.0.2.jar") ::
                    ("spark-indexedrdd-0.4.0.jar", "./jar/spark-indexedrdd-0.4.0.jar") ::
                    ("part_2.10-0.1.jar", "./jar/part_2.10-0.1.jar") ::
                    ("mongo-java-driver-3.2.2.jar", "./jar/mongo-java-driver-3.2.2.jar") ::
                    ("mongo-spark-connector_2.11-2.0.0.jar", "./jar/mongo-spark-connector_2.11-2.0.0.jar") ::
                    ("pharbers-paction-0.1.jar", "./jar/pharbers-paction-0.1.jar") :: Nil

    lazy val currentJarName = "pharbers-max-0.1"

    override def perform(args : pActionArgs): pActionArgs = {
        val sc = phSparkDriver().sc

        if(Files.exists(Paths.get(s"./target/$currentJarName.jar"))){
            if (!sc.listJars().exists(x => x.contains(currentJarName)))
                sc.addJar(s"./target/$currentJarName.jar")
        } else {
            if (!sc.listJars().exists(x => x.contains(currentJarName)))
                sc.addJar(s"./jar/$currentJarName.jar")
        }

        lst.foreach { iter =>
            if (!sc.listJars().exists(x => x.contains(iter._1))) sc.addJar(iter._2)
        }

        NULLArgs
    }
}