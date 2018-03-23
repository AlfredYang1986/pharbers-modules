package com.pharbers.sparkSteam.paction

import org.apache.hadoop.io.NullWritable
import com.pharbers.sparkSteam.paction.actionbase._
import com.pharbers.spark.driver.phSparkDriver
import com.pharbers.panel.format.input.writable.PhExcelWritable
import com.pharbers.panel.format.input.PhExcelWholeFileInputFormatWithoutIndex

object excelReadingTrait {
    def apply(path : String) : pActionTrait = new excelReadingTrait(StringArgs(path))
}

class excelReadingTrait(override val defaultArgs: pActionArgs) extends pActionTrait { //this : pFileSystem =>

    override implicit def progressFunc(progress : Double, flag : String) : Unit = {

    }

    override def perform(args : pActionArgs)(implicit f: (Double, String) => Unit) : pActionArgs = {
        val sc = phSparkDriver().sc

        RDDArgs(sc.
            newAPIHadoopFile[NullWritable, PhExcelWritable,
            PhExcelWholeFileInputFormatWithoutIndex](defaultArgs.get.toString).map (x => x._2))
    }
}
