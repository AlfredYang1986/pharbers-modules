package com.pharbers.panel.pfizer.format

import com.pharbers.panel.format.input.reader.nhwa.phNhwaCpaReader
import com.pharbers.panel.format.input.writable.PhExcelWritable
import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.NullWritable
import org.apache.hadoop.mapreduce.{InputSplit, JobContext, RecordReader, TaskAttemptContext}
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat

/**
  * Created by jeorch on 18-4-18.
  */
class phPfizerCpaFormat extends FileInputFormat[NullWritable, PhExcelWritable] {

    override def isSplitable(context: JobContext, filename: Path) : Boolean = false

    override def createRecordReader(inputSplit : InputSplit,
                                    taskAttemptContext: TaskAttemptContext) : RecordReader[NullWritable, PhExcelWritable] = {

        val reader = new phNhwaCpaReader()
        reader.initialize(inputSplit, taskAttemptContext)
        reader
    }
}