package com.pharbers.calc.alCalcMemory.aljobs

import com.pharbers.alCalcMemory.aljobs.alJob
import com.pharbers.alCalcMemory.alstages.alStage
import com.pharbers.calc.alCalcMemory.aljobs.alJobs.max_filter_excel_jobs._
import com.pharbers.calc.alCalcMemory.alprecess.alprecessdefines.alPrecessDefines._

/**
  * Created by qianpeng on 24/03/2017.
  */
class alFilterExcelJob extends alJob {

    def init(args : Map[String, Any]) = {
        val excel_file = args.get(filter_excel_path).map (x => x.toString).getOrElse(throw new Exception("have to provide excel file"))
        cur = Some(alStage(excel_file))
        process = read_excel() :: Nil
    }
}
