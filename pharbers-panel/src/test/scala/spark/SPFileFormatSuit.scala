//package spark
//
//import com.pharbers.panel.format.input.PhExcelWholeFileInputFormat
//import com.pharbers.panel.format.input.writable.PhExcelWritable
//import com.pharbers.spark.driver.phSparkDriver
//import org.apache.hadoop.io.LongWritable
//import org.apache.hadoop.io.SequenceFile.CompressionType
//import org.apache.hadoop.io.compress.GzipCodec
//import org.apache.hadoop.mapred.JobConf
//import org.apache.hadoop.mapreduce.Job
////import org.apache.hadoop.mapred.jobcontrol.Job
////import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat
//import org.apache.hadoop.mapred.SequenceFileOutputFormat
//import org.scalatest.FunSuite
//
//class SPFileFormatSuit extends FunSuite {
//    test("Spark File Convert") {
//        val sc = phSparkDriver().sc
//        sc.addJar("/Users/alfredyang/Desktop/spark/jar/commons-codec-1.5.jar")
//        sc.addJar("/Users/alfredyang/Desktop/spark/jar/dom4j-1.1.jar")
//        sc.addJar("/Users/alfredyang/Desktop/spark/jar/xmlbeans-2.3.0.jar")
//        sc.addJar("/Users/alfredyang/Desktop/spark/jar/poi-3.8.jar")
//        sc.addJar("/Users/alfredyang/Desktop/spark/jar/poi-ooxml-3.8.jar")
//        sc.addJar("/Users/alfredyang/Desktop/spark/jar/poi-ooxml-schemas-3.8.jar")
//        sc.addJar("/Users/alfredyang/Desktop/spark/jar/pharbers-panel-0.2.jar")
//        sc.addJar("/Users/alfredyang/Desktop/spark/jar/mongodb-driver-0.2.jar")
//        sc.addJar("/Users/alfredyang/Desktop/spark/jar/pharbers-page-memory-0.2.jar")
//
//        val input = sc.newAPIHadoopFile[LongWritable, PhExcelWritable, PhExcelWholeFileInputFormat]("resource/test-01.xlsx")
////        val input = sc.newAPIHadoopFile[LongWritable, PhExcelWritable, PhExcelWholeFileInputFormat]("resource/test-02.xlsx")
//
//        /**
//          * 这里是你们需要的代码，中间全部利用输入输出，转化RDD，不要使用DataFrame和DataSet这种老式SQL的东西
//          * 可以看到，input 现在就已经是RDD了，所有操作全部集成在RDD上，Spark的效能才能最优
//          * 同样，这样的写法使用Hadoop本身集成的Wriable串行化，比java本身的Object串行化的效率要高很多
//          */
//
//        input.saveAsHadoopFile[SequenceFileOutputFormat[LongWritable, PhExcelWritable]]("resource/result")
//
////        input.saveAsNewAPIHadoopFile(
////            "resource/result",
////            classOf[LongWritable],
////            classOf[PhExcelWritable],
////            classOf[SequenceFileOutputFormat[LongWritable, PhExcelWritable]],
////            job.getConfiguration)
//
//        input.saveAsTextFile("resource/compress", classOf[GzipCodec])
//
//        println(input.count())
//    }
//}
