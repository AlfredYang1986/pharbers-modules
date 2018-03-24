package com.pharbers.paction.format.input.common

import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.LongWritable
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat
import com.pharbers.panel.format.input.writable.PhExcelWritable
import com.pharbers.panel.format.input.reader.common.PhExcelWholeRecordReader
import org.apache.hadoop.mapreduce.{InputSplit, JobContext, RecordReader, TaskAttemptContext}

class PhExcelCommonInputFormat extends FileInputFormat[LongWritable, PhExcelWritable] {

    override def isSplitable(context: JobContext, filename: Path) : Boolean = false

    override def createRecordReader(inputSplit : InputSplit,
                                    taskAttemptContext: TaskAttemptContext) : RecordReader[LongWritable, PhExcelWritable] = {

        val reader = new PhExcelWholeRecordReader()
        reader.initialize(inputSplit, taskAttemptContext)
        reader
    }
}
