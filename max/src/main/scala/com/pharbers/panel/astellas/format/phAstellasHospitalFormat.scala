package com.pharbers.panel.astellas.format

import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.NullWritable
import com.pharbers.panel.format.input.reader.astellas._
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat
import com.pharbers.excel.format.input.writable.phExcelWritable
import org.apache.hadoop.mapreduce.{InputSplit, JobContext, RecordReader, TaskAttemptContext}

class phAstellasHospitalFormat extends FileInputFormat[NullWritable, phExcelWritable] {

    override def isSplitable(context: JobContext, filename: Path) : Boolean = false

    override def createRecordReader(inputSplit : InputSplit,
                                    taskAttemptContext: TaskAttemptContext) : RecordReader[NullWritable, phExcelWritable] = {

        val reader = new phAstellasHospitalReader()
        reader.initialize(inputSplit, taskAttemptContext)
        reader
    }
}