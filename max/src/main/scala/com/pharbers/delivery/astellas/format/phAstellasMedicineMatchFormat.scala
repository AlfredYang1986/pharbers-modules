package com.pharbers.delivery.astellas.format

import com.pharbers.panel.format.input.reader.astellas.phAstellasMedicineMatchReader
import com.pharbers.excel.format.input.writable.phExcelWritable
import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.NullWritable
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat
import org.apache.hadoop.mapreduce.{InputSplit, JobContext, RecordReader, TaskAttemptContext}

class phAstellasMedicineMatchFormat extends FileInputFormat[NullWritable, phExcelWritable] {

    override def isSplitable(context: JobContext, filename: Path) : Boolean = false

    override def createRecordReader(inputSplit : InputSplit,
                                    taskAttemptContext: TaskAttemptContext) : RecordReader[NullWritable, phExcelWritable] = {

        val reader = new phAstellasMedicineMatchReader()
        reader.initialize(inputSplit, taskAttemptContext)
        reader
    }
}