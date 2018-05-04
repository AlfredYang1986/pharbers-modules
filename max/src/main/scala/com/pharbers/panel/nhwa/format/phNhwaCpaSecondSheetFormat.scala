package com.pharbers.panel.nhwa.format

import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.NullWritable
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat
import com.pharbers.panel.format.input.writable.PhExcelWritable
import com.pharbers.panel.format.input.reader.nhwa.phNhwaCpaSecondSheetReader
import org.apache.hadoop.mapreduce.{InputSplit, JobContext, RecordReader, TaskAttemptContext}

class phNhwaCpaSecondSheetFormat extends FileInputFormat[NullWritable, PhExcelWritable] {

    override def isSplitable(context: JobContext, filename: Path) : Boolean = false

    override def createRecordReader(inputSplit : InputSplit,
                                    taskAttemptContext: TaskAttemptContext) : RecordReader[NullWritable, PhExcelWritable] = {

        val reader = new phNhwaCpaSecondSheetReader
        reader.setSheetindex(1)
        reader.initialize(inputSplit, taskAttemptContext)
        reader.asInstanceOf[RecordReader[NullWritable, PhExcelWritable]]
    }
}
