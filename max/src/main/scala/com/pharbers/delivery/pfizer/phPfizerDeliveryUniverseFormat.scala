package com.pharbers.delivery.pfizer

import com.pharbers.excel.format.input.writable.phExcelWritable
import com.pharbers.panel.format.input.reader.pfizer.phPfizerDeliveryUniverseReader
import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.NullWritable
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat
import org.apache.hadoop.mapreduce.{InputSplit, JobContext, RecordReader, TaskAttemptContext}

class phPfizerDeliveryUniverseFormat extends FileInputFormat[NullWritable, phExcelWritable] {

    override def isSplitable(context: JobContext, filename: Path) : Boolean = false

    override def createRecordReader(inputSplit : InputSplit,
                                    taskAttemptContext: TaskAttemptContext) : RecordReader[NullWritable, phExcelWritable] = {

        val reader = new phPfizerDeliveryUniverseReader()
        reader.initialize(inputSplit, taskAttemptContext)
        reader
    }
}