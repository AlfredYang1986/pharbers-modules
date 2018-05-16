package com.pharbers.search

/**
  * Created by jeorch on 18-5-16.
  */
trait phMaxSearchTrait {

    def getLastMonthYM(yearMonth: String): String = yearMonth.takeRight(2) match {
        case "01" => (yearMonth.take(4).toInt - 1) + "12"
        case month => if (month.toInt == 10) yearMonth.take(4) + "09" else  yearMonth.take(5) + (yearMonth.takeRight(1).toInt - 1)
    }

    def getLastSeveralMonthYM(severalCount: Int, yearMonth: String): List[String] = {
        var tempYM = yearMonth
        val lst = (1 until severalCount).map(x => {
            tempYM = getLastMonthYM(tempYM)
            tempYM
        }).toList
        yearMonth :: lst
    }

    def getLastYearYM(yearMonth: String): String = (yearMonth.take(4).toInt - 1) + yearMonth.takeRight(2)

    def getFormatValue(originValue: Double): String = f"${originValue/1.0E6}%.2f"

}
