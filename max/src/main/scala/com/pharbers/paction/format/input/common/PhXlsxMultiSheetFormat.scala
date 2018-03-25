package com.pharbers.paction.format.input.common

import com.pharbers.paction.format.input.common.multisheet._
import com.pharbers.panel.format.input.reader.common.PhExcelXLSXReader
import com.pharbers.panel.format.input.writable.PhExcelWritable
import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.NullWritable
import org.apache.hadoop.mapreduce.{InputSplit, JobContext, RecordReader, TaskAttemptContext}
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat

import scala.reflect.ClassTag

class PhXlsxMultiSheetFormat[T <: PhMultiSheet : ClassTag] extends FileInputFormat[NullWritable, PhExcelWritable] {

    override def isSplitable(context: JobContext, filename: Path) : Boolean = false

    override def createRecordReader(inputSplit : InputSplit,
                                    taskAttemptContext: TaskAttemptContext) : RecordReader[NullWritable, PhExcelWritable] = {

        val reader = new PhExcelXLSXReader
        reader.setSheetindex(PhMultiSheetFactory[T].getSheetIndex)
        reader.initialize(inputSplit, taskAttemptContext)
        reader.asInstanceOf[RecordReader[NullWritable, PhExcelWritable]]
    }
}

class PhXlsxFirstSheetFormat extends PhXlsxMultiSheetFormat[PhFirstSheet]
class PhXlsxSecondSheetFormat extends PhXlsxMultiSheetFormat[PhSecondSheet]
class PhXlsxThirdSheetFormat extends PhXlsxMultiSheetFormat[PhThirdSheet]
class PhXlsxFourthSheetFormat extends PhXlsxMultiSheetFormat[PhFourthSheet]
