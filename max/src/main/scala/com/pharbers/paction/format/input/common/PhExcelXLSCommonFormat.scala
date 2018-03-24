package com.pharbers.paction.format.input.common

import com.pharbers.panel.format.input.reader.common.PhExcelXLSReader
import com.pharbers.panel.format.input.writable.PhExcelWritable
import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.NullWritable
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat
import org.apache.hadoop.mapreduce.{InputSplit, JobContext, RecordReader, TaskAttemptContext}

class PhExcelXLSCommonFormat extends FileInputFormat[NullWritable, PhExcelWritable] {

    override def isSplitable(context: JobContext, filename: Path) : Boolean = false

    override def createRecordReader(inputSplit : InputSplit,
                                    taskAttemptContext: TaskAttemptContext) : RecordReader[NullWritable, PhExcelWritable] = {

        val reader = new PhExcelXLSReader
        reader.initialize(inputSplit, taskAttemptContext)
        reader.asInstanceOf[RecordReader[NullWritable, PhExcelWritable]]
    }
}