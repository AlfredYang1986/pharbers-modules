package com.pharbers.processSuit

import com.pharbers.pactions.actionbase.{MapArgs, NULLArgs, StringArgs}
import com.pharbers.panel.common.phBaseLine2MongoJob
import org.scalatest.FunSuite

class BaseLine2Mongo extends FunSuite {
    val nhwa = "5afa53bded925c05c6f69c54"
    val nhwa_file = "/mnt/config/MatchFile/baseline/nhwa_2017年基准线.xlsx"
    val acn = "5b023787810c6e0268fe6ff6"
    val acn_file = "/mnt/config/MatchFile/baseline/acn_2017年基准线.xlsx"
    val pfz = "5b023787810c6e0268fe6ff6"
    val pfz_file = "/mnt/config/MatchFile/baseline/pfz_2017年基准线.xlsx"


    test("base line form Excel to Mongo") {
        val df = MapArgs(
            Map(
                "company" -> StringArgs(nhwa),
                "file" -> StringArgs(nhwa_file)
//                "company" -> StringArgs(acn),
//                "file" -> StringArgs(acn_file)
//                "company" -> StringArgs(pfz),
//                "file" -> StringArgs(pfz_file)
        ))

        phBaseLine2MongoJob(df).perform(NULLArgs)
    }
}
