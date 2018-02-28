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
//    val file_base = base_path + "fea9f203d4f593a96f0d6faa91ba24ba"
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
//    test("test strange data") {
//        val file_local = file_base + "/Client/GYCX/1705 GYC.xlsx"
//        val setFieldMap = Map(
//            "城市" -> "CITY",
//            "年" -> "YEAR",
//            "月" -> "MONTH",
//            "医院编码" -> "HOSPITAL_CODE",
//            "通用名" -> "MOLE_NAME",
//            "药品商品名" -> "PRODUCT_NAME",
//            "规格" -> "PACK_DES",
//            "包装规格" -> "PACK_NUMBER",
//            "金额" -> "VALUE",
//            "最小制剂单位数量" -> "STANDARD_UNIT",
//            "剂型" -> "APP2_COD",
//            "给药途径" -> "APP1_COD",
//            "生产企业" -> "CORP_NAME"
//        )
//        implicit val cache_base = base_path + "company" + cache_local
//        implicit val postFun: Map[String, String] => Option[Map[String, String]] = { tr =>
//            val getMonth: String => String = {
//                case i if i.toInt < 10 => "0" + i
//                case i  => i
//            }
//            Some(
//                tr ++ Map(
//                    "min1" -> (tr("PRODUCT_NAME") + tr("APP2_COD") + tr("PACK_DES") + tr("PACK_NUMBER") + tr("CORP_NAME")),
//                    "YM" -> (tr("YEAR") + getMonth(tr("MONTH")))
//                )
//            )
//        }
//        implicit val filterArg = com.pharbers.panel2.util.excel.phHandleExcel.filterFun
//
//        //phHandleExcel().readExcel(phphExcelData(file_local,fieldArg = setFieldMap)).foreach(println)
//        val result = phHandleExcel().readExcelToCache(phExcelData(file_local, fieldArg = setFieldMap), "YM")
//        println(result)
//    }
//
//    test("test group by map") {
//        val testList = List(
//            Map("a" -> "1" , "b" -> "2", "c" -> 11),
//            Map("a" -> "1" , "b" -> "2", "c" -> 22),
//            Map("a" -> "1" , "b" -> "3", "c" -> 33),
//            Map("a" -> "1" , "b" -> "3", "c" -> 33)
//        )
//        val temp = testList.groupBy(x => x("a").toString + x("b").toString)
//        val temp2 = temp.map{x =>
//            x._2.head ++ Map("c" -> x._2.map(_("c").asInstanceOf[Int]).sum)
//        }
//        println(temp2)
//    }
//
//    test("test page memory") {
//        val page = pageMemory("config/company/Cache/test.cache")
//        println(s"t.size = ${page.allLength}")
//        println(s"t.pageCount = ${page.pageCount}")
//
//        page.pageData(1).zipWithIndex.foreach { x =>
//            println(s"${x._2} : ${x._1}")
//        }
//    }
//
//    test("test sort insert") {
//        val data = List(
//            "100111,空军总医院,201705,维柳芬片剂250MG60上海医药集团股份有限公司,维柳芬片剂250MG60上海医药集团股份有限公司,PHA0021131,维柳芬片剂250MG60上海医药集团股份有限公司,INF,INF,48000.0,14800.0",
//            "100112,空军总医院,201705,维柳芬片剂250MG60上海医药集团股份有限公司,维柳芬片剂250MG60上海医药集团股份有限公司,PHA0021131,维柳芬片剂250MG60上海医药集团股份有限公司,INF,INF,12000.0,3700.0",
//            "830195,新疆维吾尔自治区建工医院,201705,维柳芬片剂250MG60上海医药集团股份有限公司,维柳芬片剂250MG60上海医药集团股份有限公司,PHA0024515,维柳芬片剂250MG60上海医药集团股份有限公司,INF,INF,1200.0,416.0"
//        )
//        implicit val base_file = "config/company/Output/"
//        implicit val title = "ID" :: "Hosp_name" :: "Date" :: "Prod_Name" :: "Prod_CNAME" ::
//                "HOSP_ID" :: "Strength" :: "DOI" :: "DOIE" :: "Units" :: "Sales" :: Nil
//        val insert_data = data.map {x =>
//            title.zip(x.split(",").toList).toMap
//        }
//        val test_file = base_file + "test" :: Nil
//
//        val distinct_source: (Map[String, Any], Map[String, Any]) => Int = { (newLine, cur) =>
//            def getString(m: Map[String, Any]): String ={
//                m("ID").toString + m("Hosp_name") + m("Date") + m("Prod_Name") + m("Prod_CNAME") + m("HOSP_ID") + m("Strength") + m("DOI") + m("DOIE")
//            }
//
//            if(cur.toString == "") -1
//            else if (getString(newLine) == getString(cur)) 0
//            else if (getString(newLine) < getString(cur)) -1
//            else 1
//        }
//
//        val sameLineFun: List[Map[String, Any]] => (Map[String, Any], Map[String, Any]) = { lst =>
//            if(lst.length == 2){
//                (lst.head ++ Map(
//                    "Units" -> (lst.head("Units").toString.toDouble + lst.last("Units").toString.toDouble),
//                    "Sales" -> (lst.head("Sales").toString.toDouble + lst.last("Sales").toString.toDouble)
//                ), Map())
//            }else {
//                (lst.head, lst.last)
//            }
//        }
//
//        println("result = " + phHandleCsv().sortInsert(insert_data(0), test_file, distinct_source, sameLineFun))
//    }
//}
