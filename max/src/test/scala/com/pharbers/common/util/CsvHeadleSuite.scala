//package util
//
//import java.util.UUID
//
//import org.scalatest.FunSuite
//import com.pharbers.panel.phPanelFilePath
//import com.pharbers.panel.util.phUtilManage
//import com.pharbers.panel.util.csv.phHandleCsv
//import com.pharbers.panel.util.excel.{phExcelFileInfo, phHandleExcel}
//
///**
//  * Created by clock on 17-9-7.
//  */
//class CsvHeadleSuite extends FunSuite with phPanelFilePath {
//    val file_base = base_path + "8ee0ca24796f9b7f284d931650edbd4b"
//    val file_base2 = base_path + "fea9f203d4f593a96f0d6faa91ba24ba"
//
//    test("test writeByList") {
//        val file_local = file_base + markets_file
//        val output_file = file_base + output_local + UUID.randomUUID.toString
//        val list = phHandleExcel().read2Lst(phExcelFileInfo(file_local))
//        phHandleCsv().writeByList(list, output_file)
//    }
//
//    test("test appendByLine") {
//        val file_local = file_base + markets_file
//        val output_file = file_base + output_local + UUID.randomUUID.toString
//        val list = phHandleExcel().read2Lst(phExcelFileInfo(file_local))
//        list.foreach{x =>
//            phHandleCsv().appendByLine(x, output_file)
//        }
//    }
//
//    test("test excel2Csv") {
//        val excel = phExcelFileInfo(file_local = file_base + "/Client/" + "1612 CPA.xlsx")
//        val output_file = file_base + output_local + UUID.randomUUID.toString
//        //新建列
//        implicit val postFun: Map[String,String] => Option[Map[String, String]] = { tr =>
//            Some(tr ++ Map("AAAAA" -> tr("CITY") , "BBBBBB" -> tr("CITY"), "CCCCC" -> tr("CITY")))
//        }
//
//        phUtilManage().excel2Csv(excel, output_file)
//    }
//
//}
