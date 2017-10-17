//package util
//
//import java.util.UUID
//
//import com.pharbers.pfizer.panel_file_path
//import com.pharbers.util.csv.impl.phHandleCsvImpl
//import com.pharbers.util.excel.impl.phHandleExcelImpl
//import com.pharbers.util.excel.impl.phHandleExcelImpl._
//import org.scalatest.FunSuite
///**
//  * Created by clock on 17-9-7.
//  */
//class UtilSuite extends FunSuite {
//    val test_local = panel_file_path()
//
//    test("test get count") {
//        val file_local = test_local.path + "generatePanel" + test_local.markets_file
//        val count = phHandleExcelImpl().getCount(file_local)
//        println(s"sheet number = $count")
//    }
//
//    test("read excel => simple") {
////        val file_local = test_local.path + "generatePanel" + test_local.markets_file
//        val file_local = "/home/clock/workSpace/blackMirror/pharbers_max/calc/config/FileBase/fea9f203d4f593a96f0d6faa91ba24ba/Output/a.xlsx"
//        phHandleExcelImpl().readExcel(ExcelData(file_local)).foreach(println)
//    }
//    test("read excel => specified sheet and filter") {
//        //导入指定页数,进行filter,不设置字段名,没有默认值
//        val file_local = test_local.path + "generatePanel" + test_local.markets_file
//        implicit val filterFun: Map[String,String] => Boolean = { tr =>
//            tr.get("GYCX反馈通用名") match {
//                case None => false
//                case Some(s) if s.startsWith("曲") => true
//                case _ => false
//            }
//        }
//        val parse = phHandleExcelImpl()
//        for(i <- 1 to parse.getCount(file_local))
//            parse.readExcel(ExcelData(file_local,i)).foreach(println)
//    }
//    test("read excel => setField and setDefault") {
//        val file_local = test_local.path + "generatePanel" + test_local.markets_file
//        val setFieldMap = Map( "GYCX反馈通用名" -> "TEST" )
//        val setDefaultMap = Map(  "TA" -> "0", "TEST" -> "$TA" )
//
//        phHandleExcelImpl().readExcel(ExcelData(file_local,2,setFieldMap,setDefaultMap)).foreach(println)
//    }
//    test("read excel => post function") {
//        val file_local = test_local.path + "generatePanel" + test_local.markets_file
//        //新建列
//        implicit val postFun: Map[String,String] => Option[Map[String, String]] = { tr =>
//            Some(tr ++ Map("DOIE" -> tr("TA")))
//        }
//
//        phHandleExcelImpl().readExcel(ExcelData(file_local)).foreach(println)
//    }
//
//    test("write excel by List") {
//        val file_local = test_local.path + "generatePanel" + test_local.markets_file
//        val output_file = test_local.path + "generatePanel" + test_local.output + UUID.randomUUID.toString
//        val parse = phHandleExcelImpl()
//        Console println phHandleCsvImpl().writeByList(parse.readExcel(ExcelData(file_local)),output_file)
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
//    test("test strange data") {
//        val file_local = test_local.path + "generatePanel" + "Client/GYCX/1705 GYC.xlsx"
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
//        implicit val postFun: Map[String,String] => Option[Map[String, String]] = { tr =>
//            Some(tr ++ Map("min1" -> (tr("PRODUCT_NAME") + tr("APP2_COD") + tr("PACK_DES") + tr("PACK_NUMBER") + tr("CORP_NAME"))))
//        }
//        phHandleExcelImpl().readExcel(ExcelData(file_local,fieldArg = setFieldMap)).foreach(println)
//    }
//}
