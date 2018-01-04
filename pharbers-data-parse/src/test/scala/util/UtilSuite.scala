//package util
//
//import java.util.UUID
//
//import com.pharbers.memory.pages.pageMemory
//import com.pharbers.panel.phPanelFilePath
//import com.pharbers.panel.util.csv.phHandleCsv
//import com.pharbers.panel.util.excel.{phExcelData, phHandleExcel}
//import org.scalatest.FunSuite
//
//import scala.collection.immutable.Map
///**
//  * Created by clock on 17-9-7.
//  */
//class UtilSuite extends FunSuite with phPanelFilePath{
//    val file_base = base_path + "8ee0ca24796f9b7f284d931650edbd4b"
//    test("test get count") {
//        val file_local = file_base + markets_file
//        val count = phHandleExcel().getCount(file_local)
//        println(s"sheet number = $count")
//    }
//
//    test("read excel => simple") {
//        implicit val filterArg = com.pharbers.panel.util.excel.phHandleExcel.filterFun
//        implicit val postArg = com.pharbers.panel.util.excel.phHandleExcel.postFun
//        val file_local = file_base + markets_file
//
//        phHandleExcel().readExcel(phExcelData(file_local)).foreach(println)
//    }
//    test("read excel => specified sheet and filter") {
//        //导入指定页数,进行filter,不设置字段名,没有默认值
//        val file_local = file_base + markets_file
//        implicit val postArg = com.pharbers.panel.util.excel.phHandleExcel.postFun
//        implicit val filterFun: Map[String,String] => Boolean = { tr =>
//            tr.get("GYCX反馈通用名") match {
//                case None => false
//                case Some(s) if s.startsWith("曲") => true
//                case _ => false
//            }
//        }
//        val parse = phHandleExcel()
//        for(i <- 1 to parse.getCount(file_local))
//            parse.readExcel(phExcelData(file_local,i)).foreach(println)
//    }
//    test("read excel => setField and setDefault") {
//        implicit val filterArg = com.pharbers.panel.util.excel.phHandleExcel.filterFun
//        implicit val postArg = com.pharbers.panel.util.excel.phHandleExcel.postFun
//        val file_local = file_base + markets_file
//        val setFieldMap = Map( "GYCX反馈通用名" -> "TEST" )
//        val setDefaultMap = Map(  "TA" -> "0", "TEST" -> "$TA" )
//
//        phHandleExcel().readExcel(phExcelData(file_local,2,setFieldMap,setDefaultMap)).foreach(println)
//    }
//    test("read excel => post function") {
//        val file_local = file_base + markets_file
//        implicit val filterArg = com.pharbers.panel.util.excel.phHandleExcel.filterFun
//        //新建列
//        implicit val postFun: Map[String,String] => Option[Map[String, String]] = { tr =>
//            Some(tr ++ Map("DOIE" -> tr("TA")))
//        }
//
//        phHandleExcel().readExcel(phExcelData(file_local)).foreach(println)
//    }
//
//    test("write csv by List") {
//        implicit val filterArg = com.pharbers.panel.util.excel.phHandleExcel.filterFun
//        implicit val postArg = com.pharbers.panel.util.excel.phHandleExcel.postFun
//        val file_local = file_base + "/Manage/补充医院.xlsx"
//        val output_file = file_base + output_local + "补充医院.csv"//UUID.randomUUID.toString
//
//        Console println phHandleCsv().writeByList(phHandleExcel().readExcel(phExcelData(file_local)),output_file)
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
//        implicit val filterArg = com.pharbers.panel.util.excel.phHandleExcel.filterFun
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
