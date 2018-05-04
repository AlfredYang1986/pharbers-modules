package com.pharbers.pactions.generalactions

import scala.reflect.ClassTag
import org.apache.hadoop.io.NullWritable
import com.pharbers.pactions.actionbase._
import com.pharbers.spark.phSparkDriver
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat
import com.pharbers.panel.format.input.writable.PhExcelWritable

object xlsReadingAction {

    def apply[T <: FileInputFormat[NullWritable, PhExcelWritable] : ClassTag](arg_path: String,
                                                                              arg_name: String) : pActionTrait =
        new xlsReadingAction[T](StringArgs(arg_path), arg_name)
}

class xlsReadingAction[T <: FileInputFormat[NullWritable, PhExcelWritable] : ClassTag](override val defaultArgs: pActionArgs,
                                                                                       override val name: String) extends pActionTrait {

    override implicit def progressFunc(progress : Double, flag : String) : Unit = {}

    override def perform(args : pActionArgs)(implicit f: (Double, String) => Unit) : pActionArgs = {
        val sc = phSparkDriver().sc

        RDDArgs(sc.
            newAPIHadoopFile[NullWritable, PhExcelWritable,
            T](defaultArgs.get.toString).map (x => x._2))
    }
}
