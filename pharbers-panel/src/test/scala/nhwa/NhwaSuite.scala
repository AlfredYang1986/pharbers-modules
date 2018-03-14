//package nhwa
//
//import java.io.File
//import java.util.Date
//import org.scalatest.FunSuite
//import java.text.SimpleDateFormat
//import scala.collection.immutable.Map
//import com.pharbers.panel.util.phUtilManage
//import play.api.libs.json.{JsString, JsValue}
//import com.pharbers.spark.driver.phSparkDriver
//import com.pharbers.panel.util.excel.phExcelFileInfo
//import com.pharbers.panel.{phPanelFilePath, phPanelHeadle}
//
///**
//  * Created by clock on 18-1-3.
//  */
//class NhwaSuite extends FunSuite with phPanelFilePath {
//    val sparkDriver = phSparkDriver()
//    val company_name = "8ee0ca24796f9b7f284d931650edbd4b"
//    val file_base = base_path + company_name
//    val cpa_name = "171016恩华2017年8月检索.xlsx"
//    val args: Map[String, List[String]] = Map(
//        "company" -> List(company_name),
//        "uid" -> List("08f1517cd192c5d8f9290c46418e08b1"),
//        "cpas" -> List(cpa_name),
//        "gycxs" -> List("")
//    )
//
//    test("test excel2Csv => cpa") {
//        val cpa_name = "180211恩华17年1-12月检索.xlsx"
//        val input_file = file_base + client_path + cpa_name
//        val output_file = file_base + output_path + cpa_name.replace(".xlsx", ".csv")
//
//        val setFieldMap = Map(
//            "省" -> "PROVINCES",
//            "城市" -> "CITY",
//            "年" -> "YEAR",
//            "季度" -> "QUARTER",
//            "月" -> "MONTH",
//            "医院编码" -> "HOSPITAL_CODE",
//            "ATC编码" -> "ATC_CODE",
//            "ATC码" -> "ATC_CODE",
//            "药品名称" -> "MOLE_NAME",
//            "商品名" -> "PRODUCT_NAME",
//            "包装" -> "PACKAGE",
//            "药品规格" -> "PACK_DES",
//            "规格" -> "PACK_DES",
//            "包装数量" -> "PACK_NUMBER",
//            "金额（元）" -> "VALUE",
//            "数量（支/片）" -> "STANDARD_UNIT",
//            "剂型" -> "APP2_COD",
//            "给药途径" -> "APP1_COD",
//            "途径" -> "APP1_COD",
//            "生产企业" -> "CORP_NAME"
//        )
//        val setDefaultMap = Map(
//            "PRODUCT_NAME" -> "$MOLE_NAME",
//            "VALUE" -> "0",
//            "STANDARD_UNIT" -> "0"
//        )
//        val getMonth: String => String = {
//            case i if i.toInt < 10 => "0" + i
//            case i => i
//        }
//        val excel = phExcelFileInfo(file_local = input_file, fieldArg = setFieldMap, defaultValueArg = setDefaultMap)
//        implicit val postFun: Map[String, String] => Option[Map[String, String]] = { tr =>
//            Some(
//                tr ++ Map(
//                    "min1" -> (tr("PRODUCT_NAME") + tr("APP2_COD") + tr("PACK_DES") + tr("PACK_NUMBER") + tr("CORP_NAME")),
//                    "YM" -> (tr("YEAR") + getMonth(tr("MONTH")))
//                )
//            )
//        }
//
//        phUtilManage().excel2Csv(excel, output_file)
//    }
//
//    test("spark read csv in hdfs => cpa") {
//        val file_path = "hdfs://192.168.100.174:12138/user/jeorch/180211恩华17年1-12月检索.csv"
//        val rdd = phSparkDriver().csv2RDD(file_path, delimiter = 31.toChar.toString, true)
//        rdd.show(false)
//    }
//
//    test("test calcYM") {
//        val file_path = "hdfs://192.168.100.174:12138/user/jeorch/180211恩华17年1-12月检索.csv"
//        val rdd = sparkDriver.csv2RDD(file_path, delimiter = 31.toChar.toString, true)
//        val temp = rdd.groupBy("YM", "HOSPITAL_CODE").count()
//                .groupBy("YM").count()
//                .collect()
//                .map{row => (row.getString(0), row.getLong(1))}
//        val max = temp.map(_._2).max
//        val result = temp.filter(_._2 > max/2).map(_._1).sorted
//        result.foreach(println)
//    }
//
//    test("test excel2Csv => 未到医院") {
//        val cpa_name = "180211恩华17年1-12月检索.xlsx"
//        val input_file = file_base + client_path + cpa_name
//        val output_file = file_base + output_path + "未到医院名单.csv"
//
//        val excel = phExcelFileInfo(file_local = input_file, 2)
//
//        phUtilManage().excel2Csv(excel, output_file)
//    }
//
//    test("test excel2Csv => 补充医院") {
//        val file_name = "补充医院.xlsx"
//        val input_file = file_base + client_path + file_name
//        val output_file = file_base + output_path + file_name.replace(".xlsx", ".csv")
//
//        val setFieldMap = Map(
//            "省" -> "PROVINCES",
//            "城市" -> "CITY",
//            "年" -> "YEAR",
//            "季度" -> "QUARTER",
//            "月" -> "MONTH",
//            "医院编码" -> "HOSPITAL_CODE",
//            "ATC编码" -> "ATC_CODE",
//            "ATC码" -> "ATC_CODE",
//            "药品名称" -> "MOLE_NAME",
//            "商品名" -> "PRODUCT_NAME",
//            "包装" -> "PACKAGE",
//            "药品规格" -> "PACK_DES",
//            "规格" -> "PACK_DES",
//            "包装数量" -> "PACK_NUMBER",
//            "金额（元）" -> "VALUE",
//            "数量（支/片）" -> "STANDARD_UNIT",
//            "剂型" -> "APP2_COD",
//            "给药途径" -> "APP1_COD",
//            "途径" -> "APP1_COD",
//            "生产企业" -> "CORP_NAME"
//        )
//        val getMonth: String => String = {
//            case i if i.toInt < 10 => "0" + i
//            case i => i
//        }
//
//        val excel = phExcelFileInfo(file_local = input_file, fieldArg = setFieldMap)
//        implicit val postFun: Map[String, String] => Option[Map[String, String]] = { tr =>
//            Some(
//                tr ++ Map(
//                    "YM" -> (tr("YEAR") + getMonth(tr("MONTH")))
//                )
//            )
//        }
//
//        phUtilManage().excel2Csv(excel, output_file)
//    }
//
//    test("test excel2Csv => 匹配表") {
//        val file_name = "匹配表.xlsx"
//        val input_file = file_base + "/Manage/" + file_name
//        val output_file = file_base + output_path + file_name.replace(".xlsx", ".csv")
//
//        implicit val postFun: Map[String, String] => Option[Map[String, String]] = { tr =>
//            Some(
//                Map("min1" -> tr("min1"), "min1_标准" -> tr("min1_标准"), "通用名" -> tr("药品名称"))
//            )
//        }
//
//        val excel = phExcelFileInfo(file_local = input_file)
//        phUtilManage().excel2Csv(excel, output_file)
//    }
//
//    test("test excel2Csv => universe") {
//        val file_name = "universe_麻醉市场_online.xlsx"
//        val input_file = file_base + "/Manage/matchFile/" + file_name
//        val output_file = file_base + output_path + file_name.replace(".xlsx", ".csv")
//
//        val setFieldMap = Map(
//            "样本医院编码" -> "ID",
//            "PHA医院名称" -> "HOSP_NAME",
//            "PHA ID" -> "HOSP_ID",
//            "市场" -> "DOI"
//        )
//        implicit val postFun: Map[String, String] => Option[Map[String, String]] = { tr =>
//            Some(
//                Map(
//                    "ID" -> tr("ID"),
//                    "HOSP_NAME" -> tr("HOSP_NAME"),
//                    "HOSP_ID" -> tr("HOSP_ID"),
//                    "DOI" -> tr("DOI"),
//                    "DOIE" -> tr("DOI")
//                )
//            )
//        }
//        implicit val filter: Map[String, String] => Boolean = { tr =>
//            tr.get("If Panel_All") match {
//                case None => false
//                case Some(s) if s == "1" => true
//                case _ => false
//            }
//        }
//
//        val excel = phExcelFileInfo(file_local = input_file, fieldArg = setFieldMap)
//        phUtilManage().excel2Csv(excel, output_file)
//    }
//
//    test("test excel2Csv => 通用名市场定义") {
//        val file_name = "通用名市场定义.xlsx"
//        val input_file = file_base + "/Manage/" + file_name
//        val output_file = file_base + output_path + file_name.replace(".xlsx", ".csv")
//
//        val excel = phExcelFileInfo(file_local = input_file, sheetName = "Sheet1")
//        phUtilManage().excel2Csv(excel, output_file)
//    }
//
//    test("test excel2Csv => 未出版医院名单") {
//        val file_name = "2017年未出版医院名单.xlsx"
//        val input_file = file_base + "/Manage/" + file_name
//        val output_file = file_base + output_path + file_name.replace(".xlsx", ".csv")
//
//        val excel = phExcelFileInfo(file_local = input_file)
//        phUtilManage().excel2Csv(excel, output_file)
//    }
//
//    test("test get markets") {
//        val mktHander = phPanelHeadle(args)
//        println("mkt = " + mktHander.getMarkets)
//    }
//
//    test("test calc ym") {
//        val file_path = "hdfs://192.168.100.174:12138/user/jeorch/180211恩华17年1-12月检索.csv"
//        val ymHander = phPanelHeadle(args)
//        val yms = ymHander.calcYM.asInstanceOf[JsString].value
//        val lst = yms.split(",").toList
//        println(lst)
//    }
//
//    test("test load m1") {
//        val cpa_location: String = "hdfs://192.168.100.174:12138/user/jeorch/180211恩华17年1-12月检索.csv"
//        val m1_location = "hdfs://192.168.100.174:12138/user/jeorch/匹配表.csv"
//        val hos_location = "hdfs://192.168.100.174:12138/user/jeorch/universe_麻醉市场_online.csv"
//        val b0_location = "hdfs://192.168.100.174:12138/user/jeorch/通用名市场定义.csv"
//        val not_arrival_hosp_location = "hdfs://192.168.100.174:12138/user/jeorch/未到医院名单.csv"
//        val not_published_hosp_location = "hdfs://192.168.100.174:12138/user/jeorch/2017年未出版医院名单.csv"
//        val full_hosp_location = "hdfs://192.168.100.174:12138/user/jeorch/补充医院.csv"
//        val c0 = sparkDriver.csv2RDD(cpa_location, delimiter = 31.toChar.toString)
//        val m1 = sparkDriver.csv2RDD(m1_location, delimiter = 31.toChar.toString)
//        val hos00 = sparkDriver.csv2RDD(hos_location, delimiter = 31.toChar.toString)
//        val b0 = sparkDriver.csv2RDD(b0_location, delimiter = 31.toChar.toString)
//        val nah = sparkDriver.csv2RDD(not_arrival_hosp_location, delimiter = 31.toChar.toString)
//        val nph = sparkDriver.csv2RDD(not_published_hosp_location, delimiter = 31.toChar.toString)
//        val fh = sparkDriver.csv2RDD(full_hosp_location, delimiter = 31.toChar.toString)
//
////        val r = c0_rdd.map(x => x.getAs[String]("HOSPITAL_CODE"))
////        r.foreach(println)
////        val rr=rdd0.filter(x => hos0.contains(rdd0("HOSPITAL_CODE")))
////        c0_rdd.un
////        println("cpa count = " + rr.count())
////        val c0 = rdd0.filter(x => hos0.contains(rdd0("HOSPITAL_CODE")))
////        println("c0 count = " + c0.count())
//    }
//
//    test("join in impliment remove date") {
//        val A = Seq(("A", "1"), ("B", "2"), ("C", "3"), ("A", "4"))
//        val B = Seq("A", "B")
//        import sparkDriver.ss.implicits._
//        val a = A.toDF("A", "B")
//        val b = B.toDF("C")
//        val c = a.join(b, a("A") equalTo b("C")).drop("C")
//        c.show()
//    }
//
//    test("test union") {
//        val A = Seq(("A", "1"), ("B", "2"), ("C", "3"), ("A", "4"))
//        val B = Seq(("1", "A"), ("2", "B"), ("3", "C"), ("4", "D"))
//        import sparkDriver.ss.implicits._
//        val a = A.toDF("A", "B")
//        val b = B.toDF("B", "A").select(a.columns.head, a.columns.tail:_*)
//        val c = a.union(b)
//        c.show()
//    }
//
//    test("test move file") {
//        val panel_location = "/home/jeorch/clock_file/panel3"
//
//        def getAllFile(dir: String): Array[String] = {
//            val list = new File(dir).listFiles()
//            list.flatMap {file =>
//                if (file.isDirectory) {
//                    getAllFile(file.getAbsolutePath)
//                } else {
//                    Array(file.getAbsolutePath)
//                }
//            }
//        }
//
//        def delFile(dir: String): Unit = {
//            val parent = new File(dir)
//            val list = parent.listFiles()
//            list.foreach {file =>
//                if (file.isDirectory) {
//                    delFile(file.getAbsolutePath)
//                } else {
//                    file.delete()
//                }
//            }
//            parent.delete()
//        }
//
//        val tempFile = getAllFile(panel_location).find(_.endsWith(".csv")) match {
//            case None => throw new Exception("未找到文件")
//            case Some(file) => file
//        }
//
//        new File(tempFile).renameTo(new File(panel_location + ".csv"))
//
//        delFile(panel_location)
//    }
//
//    test("test generate panel file") {
//        val dateformat = new SimpleDateFormat("MM-dd HH:mm:ss")
//        println(s"生成panel测试开始时间" + dateformat.format(new Date()))
//        println()
//
//        def getResult(data: JsValue) = {
//            data.as[Map[String, JsValue]].map { x =>
//                x._1 -> x._2.as[Map[String, JsValue]].map { y =>
//                    y._1 -> y._2.as[List[String]]
//                }
//            }
//        }
//
//        val panelHander = phPanelHeadle(args)
//        val result = getResult(panelHander.getPanelFile(List("201712")))
//        println("result = " + result)
//        println()
//        println(s"生成panel测试结束时间" + dateformat.format(new Date()))
//    }
//
//    test("Pressure test => 150") {
//        val dateformat = new SimpleDateFormat("MM-dd HH:mm:ss")
//        println(s"压力测试开始时间" + dateformat.format(new Date()))
//        println()
//
//        def getResult(data: JsValue) = {
//            data.as[Map[String, JsValue]].map { x =>
//                x._1 -> x._2.as[Map[String, JsValue]].map { y =>
//                    y._1 -> y._2.as[List[String]]
//                }
//            }
//        }
//
//        val panelHander = phPanelHeadle(args)
//
//        for (i <- 1 to 1) {
//            val result = getResult(panelHander.getPanelFile(List("201708")))
//            val panelLst = result.values.flatMap(_.values).toList.flatten
//            print(s"panel $i = " + panelLst.mkString(","))
//        }
//
//        println()
//        println(s"压力测试结束时间" + dateformat.format(new Date()))
//    }
//}
