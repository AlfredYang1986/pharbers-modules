package com.pharbers.panel.nhwa.format

import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.NullWritable
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat
import com.pharbers.excel.format.input.writable.phExcelWritable
import com.pharbers.panel.format.input.reader.nhwa.phNhwaCpaReader
import org.apache.hadoop.mapreduce.{InputSplit, JobContext, RecordReader, TaskAttemptContext}

class phNhwaCpaFormat extends FileInputFormat[NullWritable, phExcelWritable] {

    override def isSplitable(context: JobContext, filename: Path) : Boolean = false

    override def createRecordReader(inputSplit : InputSplit,
                                    taskAttemptContext: TaskAttemptContext) : RecordReader[NullWritable, phExcelWritable] = {

        val reader = new phNhwaCpaReader()
        reader.initialize(inputSplit, taskAttemptContext)
        reader
    }
}