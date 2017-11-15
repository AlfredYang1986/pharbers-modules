package com.pharbers.alCalcMemory.aljobs

import java.util.UUID
import com.pharbers.alCalcMemory.alstages.alStage
import com.pharbers.alCalcMemory.alprecess.alPrecess
import com.pharbers.alCalcMemory.alOther.alLog.alLoggerMsgTrait

/**
  * Created by Alfred on 10/03/2017.
  */
class job_defines(val t : Int, val d : String)

trait alJob extends alLoggerMsgTrait {
    val uuid = UUID.randomUUID.toString

    var cur : Option[alStage] = None
    var process : List[alPrecess] = Nil

    def init(args : Map[String, Any])
    def clean = Unit
    
    def result : Option[Any] =  {
        if (!process.isEmpty)
            nextAcc
        None
    }

    def nextAcc : Unit = {             // 递归实现next
        if (!process.isEmpty) {
            val p = process.head
            logger.info(s"current precess is $p")
            process = process.tail

            val s = cur.map (x => x).getOrElse(throw new Exception("job needs current stage"))
            logger.info(s"current stage is $s")
            val s1 = p.precess(s).head
            cur = Some(s1)
            logger.info(s"new stage is $s1")
            if (s1.canLength)
                logger.info(s"if calc new stage has ${s1.length} data")

            nextAcc
        }
    }

    /**
      * 拆分job，用户计算
      */
    var subJobs : Option[List[alJob]] = None
}
