package com.pharbers.sparkSteam.paction

import com.pharbers.spark.driver.phSparkDriver
import com.pharbers.sparkSteam.paction.actionbase.{NULLArgs, pActionArgs, pActionTrait}

object jarPreloadTrait {
    def apply() : pActionTrait = new jarPreloadTrait()
}

class jarPreloadTrait extends pActionTrait { //this : pFileSystem =>

    override val defaultArgs : pActionArgs = NULLArgs

    lazy val lst =  ("commons-codec-1.5.jar", "/Users/alfredyang/Desktop/spark/jar/commons-codec-1.5.jar") ::
                    ("dom4j-1.1.jar", "/Users/alfredyang/Desktop/spark/jar/dom4j-1.1.jar") ::
                    ("xmlbeans-2.3.0.jar", "/Users/alfredyang/Desktop/spark/jar/xmlbeans-2.3.0.jar") ::
                    ("poi-3.8.jar", "/Users/alfredyang/Desktop/spark/jar/poi-3.8.jar") ::
                    ("poi-ooxml-3.8.jar", "/Users/alfredyang/Desktop/spark/jar/poi-ooxml-3.8.jar") ::
                    ("poi-ooxml-schemas-3.8.jar", "/Users/alfredyang/Desktop/spark/jar/poi-ooxml-schemas-3.8.jar") ::
                    ("pharbers-max-0.1.jar", "/Users/alfredyang/.m2/repository/com/pharbers/pharbers-max/0.1/pharbers-max-0.1.jar") :: Nil

    override implicit def progressFunc(progress : Double, flag : String) : Unit = {

    }

    override def perform(args : pActionArgs)(implicit f: (Double, String) => Unit) : pActionArgs = {
        val sc = phSparkDriver().sc

        lst.foreach { iter =>
            if (!sc.listJars().contains(iter._1))
                sc.addJar(iter._2)
        }

        NULLArgs
    }
}