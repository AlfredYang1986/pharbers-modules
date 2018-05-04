package com.pharbers.panel.test.samplingtest

import com.pharbers.unitTest.getResult.{GetResult, compareResult}
import com.pharbers.unitTest.readFiles.{ReadCSVFile, ReadExcelFile}
import org.specs2.specification.core.Fragment

class ContrastTest extends org.specs2.mutable.Specification {
    val result = GetResult().getResult
    result.indices.foreach(i =>
        //        s"test ${i+1} 开始测试" >> {
        s"开始对比 ${result(i)("resultMatch_file")}" >> {
            val csvResult = ReadCSVFile(result(i)("maxResult"), ReadExcelFile(result(i)("resultMatch_file")).getArea()).getCsvResult()
            val excelResult = ReadExcelFile(result(i)("resultMatch_file")).getExcelResult
            Fragment.foreach(csvResult.indices) { j =>
                "Row " + (j + 3) ! {
                    compareResult().isSame(excelResult(j), csvResult(j)) must_== true
                }
            }
        }
    )
}
