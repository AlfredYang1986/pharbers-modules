package com.pharbers.panel.pfizer.format

import com.pharbers.panel.format.input.reader.pfizer.phPfizerGycxReader
import com.pharbers.excel.format.input.writable.phExcelWritable
import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.NullWritable
import org.apache.hadoop.mapreduce.{InputSplit, JobContext, RecordReader, TaskAttemptContext}
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat

/**
  * Created by jeorch on 18-4-23.
  */
class phPfizerGycxFormat extends FileInputFormat[NullWritable, phExcelWritable] {

    override def isSplitable(context: JobContext, filename: Path) : Boolean = false

    override def createRecordReader(inputSplit : InputSplit,
                                    taskAttemptContext: TaskAttemptContext) : RecordReader[NullWritable, phExcelWritable] = {

        val reader = new phPfizerGycxReader()
        reader.initialize(inputSplit, taskAttemptContext)
        reader
    }
}