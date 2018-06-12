package com.pharbers.panel.astellas.format

import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.NullWritable
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat
import com.pharbers.excel.format.input.writable.phExcelWritable
import com.pharbers.panel.format.input.reader.astellas.phAstellasProductMatchReader
import org.apache.hadoop.mapreduce.{InputSplit, JobContext, RecordReader, TaskAttemptContext}

class phAstellasProductMatchFormat extends FileInputFormat[NullWritable, phExcelWritable] {

    override def isSplitable(context: JobContext, filename: Path) : Boolean = false

    override def createRecordReader(inputSplit : InputSplit,
                                    taskAttemptContext: TaskAttemptContext) : RecordReader[NullWritable, phExcelWritable] = {

        val reader = new phAstellasProductMatchReader()
        reader.initialize(inputSplit, taskAttemptContext)
        reader
    }
}