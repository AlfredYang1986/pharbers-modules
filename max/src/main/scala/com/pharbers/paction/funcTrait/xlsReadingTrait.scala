package com.pharbers.paction.funcTrait

import org.apache.hadoop.io.NullWritable
import com.pharbers.paction.actionbase._
import com.pharbers.spark.driver.phSparkDriver
import com.pharbers.panel.format.input.writable.PhExcelWritable
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat

import scala.reflect.ClassTag

object xlsReadingTrait {
    def apply[T <: FileInputFormat[NullWritable, PhExcelWritable] : ClassTag](path : String) : pActionTrait = new xlsReadingTrait[T](StringArgs(path))

    def apply[T <: FileInputFormat[NullWritable, PhExcelWritable] : ClassTag](path : String, name : String) : pActionTrait = {
        val tmp = new xlsReadingTrait[T](StringArgs(path))
        tmp.name = name
        tmp
    }
}

class xlsReadingTrait[T <: FileInputFormat[NullWritable, PhExcelWritable] : ClassTag](override val defaultArgs: pActionArgs) extends pActionTrait { //this : pFileSystem =>

    override implicit def progressFunc(progress : Double, flag : String) : Unit = {

    }

    override def perform(args : pActionArgs)(implicit f: (Double, String) => Unit) : pActionArgs = {
        val sc = phSparkDriver().sc

        RDDArgs(sc.
            newAPIHadoopFile[NullWritable, PhExcelWritable,
            T](defaultArgs.get.toString).map (x => x._2))
    }
}
