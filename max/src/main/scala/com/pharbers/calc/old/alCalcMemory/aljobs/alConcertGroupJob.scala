package com.pharbers.calc.old.alCalcMemory.aljobs

import com.pharbers.alCalcMemory.aljobs.alJob
import com.pharbers.alCalcMemory.alstages.alStage
import com.pharbers.calc.old.alCalcHelp.alShareData
import com.pharbers.calc.old.alCalcMemory.alprecess.alprecessdefines.alPrecessDefines._
import com.pharbers.common.another_file_package.fileConfig._

/**
  * Created by Alfred on 11/03/2017.
  */
class alConcertGroupJob(u : String, val parent : String) extends alJob {
    override val uuid: String = u

    override def init(args : Map[String, Any]) = {
        val restore_path = s"${memorySplitFile}${sync}$parent/$uuid"
        cur = Some(alStage(restore_path))
        process = restore_data() :: do_map (alShareData.txt2IntegratedData(_)) :: do_calc() :: Nil
    }
}