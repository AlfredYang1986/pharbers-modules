package com.pharbers.sparkSteam.paction

import org.apache.hadoop.io.NullWritable
import com.pharbers.sparkSteam.paction.actionbase._
import com.pharbers.spark.driver.phSparkDriver
import com.pharbers.panel.format.input.writable.PhExcelWritable
import com.pharbers.panel.format.input.PhExcelWholeFileInputFormatWithoutIndex

object excelReadingTrait {
    def apply(path : String) : pActionTrait = new excelReadingTrait(StringArgs(path))
}


class excelReadingTrait(override val defaultArgs: pActionArgs) extends pActionTrait { //this : pFileSystem =>

    override implicit def progressFunc(progress : Double, flag : String) : Unit = {

    }

    override def perform(args : pActionArgs)(implicit f: (Double, String) => Unit) : pActionArgs = {
        val sc = phSparkDriver().sc
        sc.addJar("/Users/alfredyang/Desktop/spark/jar/commons-codec-1.5.jar")
        sc.addJar("/Users/alfredyang/Desktop/spark/jar/dom4j-1.1.jar")
        sc.addJar("/Users/alfredyang/Desktop/spark/jar/xmlbeans-2.3.0.jar")
        sc.addJar("/Users/alfredyang/Desktop/spark/jar/poi-3.8.jar")
        sc.addJar("/Users/alfredyang/Desktop/spark/jar/poi-ooxml-3.8.jar")
        sc.addJar("/Users/alfredyang/Desktop/spark/jar/poi-ooxml-schemas-3.8.jar")
        sc.addJar("/Users/alfredyang/.m2/repository/com/pharbers/pharbers-max/0.1/pharbers-max-0.1.jar")

        RDDArgs(sc.
            newAPIHadoopFile[NullWritable, PhExcelWritable,
            PhExcelWholeFileInputFormatWithoutIndex](defaultArgs.get.toString).map (x => x._2))
    }

}
