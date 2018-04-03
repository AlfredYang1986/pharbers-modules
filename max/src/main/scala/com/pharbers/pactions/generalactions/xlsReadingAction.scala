package com.pharbers.pactions.generalactions

import org.apache.hadoop.io.NullWritable
import com.pharbers.pactions.actionbase._
import com.pharbers.panel.format.input.writable.PhExcelWritable
import com.pharbers.spark.phSparkDriver
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat

import scala.reflect.ClassTag

object xlsReadingAction {
    def apply[T <: FileInputFormat[NullWritable, PhExcelWritable] : ClassTag](path : String) : pActionTrait = new xlsReadingAction[T](StringArgs(path))

    def apply[T <: FileInputFormat[NullWritable, PhExcelWritable] : ClassTag](path : String, name : String) : pActionTrait = {
        val tmp = new xlsReadingAction[T](StringArgs(path))
        tmp.name = name
        tmp
    }
}

class xlsReadingAction[T <: FileInputFormat[NullWritable, PhExcelWritable] : ClassTag](override val defaultArgs: pActionArgs) extends pActionTrait { //this : pFileSystem =>

    override implicit def progressFunc(progress : Double, flag : String) : Unit = {

    }

    override def perform(args : pActionArgs)(implicit f: (Double, String) => Unit) : pActionArgs = {
        val sc = phSparkDriver().sc

        RDDArgs(sc.
            newAPIHadoopFile[NullWritable, PhExcelWritable,
            T](defaultArgs.get.toString).map (x => x._2))
    }
}
