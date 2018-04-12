package com.pharbers.panel.astellas.format

import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.NullWritable
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat
import com.pharbers.panel.format.input.writable.PhExcelWritable
import com.pharbers.panel.format.input.reader.astellas.phAstellasMarketsMatchReader
import org.apache.hadoop.mapreduce.{InputSplit, JobContext, RecordReader, TaskAttemptContext}

class phAstellasMarketsMatchFormat extends FileInputFormat[NullWritable, PhExcelWritable] {

    override def isSplitable(context: JobContext, filename: Path) : Boolean = false

    override def createRecordReader(inputSplit : InputSplit,
                                    taskAttemptContext: TaskAttemptContext) : RecordReader[NullWritable, PhExcelWritable] = {

        val reader = new phAstellasMarketsMatchReader()
        reader.initialize(inputSplit, taskAttemptContext)
        reader
    }
}