package com.pharbers.paction

import com.pharbers.paction.actionbase.{NULLArgs, StringArgs, pActionArgs, pActionTrait}
import com.pharbers.panel.format.input.PhExcelWholeFileInputFormat
import com.pharbers.panel.format.input.writable.PhExcelWritable
import com.pharbers.pfs.pFileSystem
import com.pharbers.spark.driver.phSparkDriver
import org.apache.hadoop.io.LongWritable
import org.apache.hadoop.io.compress.GzipCodec
import org.apache.hadoop.mapred.SequenceFileOutputFormat

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
        sc.addJar("/Users/alfredyang/Desktop/spark/jar/pharbers-panel-0.2.jar")
        sc.addJar("/Users/alfredyang/Desktop/spark/jar/mongodb-driver-0.2.jar")
        sc.addJar("/Users/alfredyang/Desktop/spark/jar/pharbers-page-memory-0.2.jar")

        val input = sc.newAPIHadoopFile[LongWritable, PhExcelWritable, PhExcelWholeFileInputFormat]("resource/test-01.xlsx")

        /**
          * 这里是你们需要的代码，中间全部利用输入输出，转化RDD，不要使用DataFrame和DataSet这种老式SQL的东西
          * 可以看到，input 现在就已经是RDD了，所有操作全部集成在RDD上，Spark的效能才能最优
          * 同样，这样的写法使用Hadoop本身集成的Wriable串行化，比java本身的Object串行化的效率要高很多
          */

        input.saveAsHadoopFile[SequenceFileOutputFormat[LongWritable, PhExcelWritable]]("resource/result")

        //        input.saveAsNewAPIHadoopFile(
        //            "resource/result",
        //            classOf[LongWritable],
        //            classOf[PhExcelWritable],
        //            classOf[SequenceFileOutputFormat[LongWritable, PhExcelWritable]],
        //            job.getConfiguration)

        input.saveAsTextFile("resource/compress", classOf[GzipCodec])
        NULLArgs
    }

}
