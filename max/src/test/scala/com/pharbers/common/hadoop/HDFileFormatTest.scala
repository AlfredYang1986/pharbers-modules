//package hadoop
//
//import com.pharbers.panel.format.input.PhExcelWholeFileInputFormat
//import com.pharbers.excel.format.input.writable.phExcelWritable
//import org.apache.hadoop.conf.Configured
//import org.apache.hadoop.fs.Path
//import org.apache.hadoop.io.SequenceFile.CompressionType
//import org.apache.hadoop.io.{DoubleWritable, LongWritable, NullWritable, Text}
//import org.apache.hadoop.mapreduce.Job
//import org.apache.hadoop.mapreduce.lib.input.FileInputFormat
//import org.apache.hadoop.mapreduce.lib.output.{FileOutputFormat, SequenceFileOutputFormat}
//import org.apache.hadoop.util.Tool
//
//class HDFileFormatTest extends Configured with Tool {
//
//    override def run(args: Array[String]): Int = {
//        val job = new Job(getConf, "pharbers file convert")
//        job.setJarByClass(getClass)
//
//        job.setOutputKeyClass(classOf[LongWritable])
//        job.setOutputValueClass(classOf[phExcelWritable])
//
//        job.setInputFormatClass(classOf[PhExcelWholeFileInputFormat])
//        // job.setOutputFormatClass(classOf[SequenceFileOutputFormat[DoubleWritable, Text]])
//
//        FileInputFormat.addInputPath(job, new Path("resource/test.xlsx"))
//
//        FileOutputFormat.setOutputPath(job, new Path("resource/result"))
//        FileOutputFormat.setCompressOutput(job, true)
//        SequenceFileOutputFormat.setOutputCompressionType(job, CompressionType.BLOCK)
//
//        if (job.waitForCompletion(true)) 0
//        else 1
//    }
//}
