package com.pharbers.panel.format.input

import com.pharbers.panel.format.input.writable.PhExcelWritable
import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.{BytesWritable, LongWritable, NullWritable}
import org.apache.hadoop.mapreduce.{InputSplit, JobContext, RecordReader, TaskAttemptContext}
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat

class PhExcelWholeFileInputFormat extends FileInputFormat[LongWritable, PhExcelWritable] {

    override def isSplitable(context: JobContext, filename: Path) : Boolean = false

    override def createRecordReader(inputSplit : InputSplit,
                                    taskAttemptContext: TaskAttemptContext) : RecordReader[LongWritable, PhExcelWritable] = {

        val reader = new PhExcelWholeRecordReader()
        reader.initialize(inputSplit, taskAttemptContext)
        reader
    }
}
