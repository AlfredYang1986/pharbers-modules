package com.pharbers.panel2.astellas.format

import com.pharbers.panel.format.input.reader.astellas._
import com.pharbers.panel.format.input.writable.PhExcelWritable
import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.NullWritable
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat
import org.apache.hadoop.mapreduce.{InputSplit, JobContext, RecordReader, TaskAttemptContext}

class phAstellasHospitalFormat extends FileInputFormat[NullWritable, PhExcelWritable] {

    override def isSplitable(context: JobContext, filename: Path) : Boolean = false

    override def createRecordReader(inputSplit : InputSplit,
                                    taskAttemptContext: TaskAttemptContext) : RecordReader[NullWritable, PhExcelWritable] = {

        val reader = new phAstellasHospitalReader()
        reader.initialize(inputSplit, taskAttemptContext)
        reader
    }
}