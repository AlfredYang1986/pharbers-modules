package com.pharbers.sparkSteam.paction

import com.pharbers.panel.format.input.nhwa.PhExcelNhwaFormat
import org.apache.hadoop.io.NullWritable
import com.pharbers.sparkSteam.paction.actionbase._
import com.pharbers.spark.driver.phSparkDriver
import com.pharbers.panel.format.input.writable.PhExcelWritable
//import com.pharbers.panel.format.input.PhExcelWholeFileInputFormatWithoutIndex
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat

object excelReadingTrait {
    def apply[T <: FileInputFormat[NullWritable, PhExcelWritable]](path : String) : pActionTrait = new excelReadingTrait[T](StringArgs(path))
}

class excelReadingTrait[T <: FileInputFormat[NullWritable, PhExcelWritable]](override val defaultArgs: pActionArgs) extends pActionTrait { //this : pFileSystem =>

    override implicit def progressFunc(progress : Double, flag : String) : Unit = {

    }

    override def perform(args : pActionArgs)(implicit f: (Double, String) => Unit) : pActionArgs = {
        val sc = phSparkDriver().sc

        val tmp =
        RDDArgs(sc.
            newAPIHadoopFile[NullWritable, PhExcelWritable,
            T](defaultArgs.get.toString).map (x => x._2))

        tmp.get.saveAsTextFile("resource/result")
        NULLArgs
    }
}
