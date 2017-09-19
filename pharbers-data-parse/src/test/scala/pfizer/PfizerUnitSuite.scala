//package pfizer
//
//import java.text.SimpleDateFormat
//import java.util.Date
//
//import com.mongodb.casbah.commons.MongoDBObject
//import com.pharbers.mongodbConnect._data_connection
//import com.pharbers.util.excel.impl.phHandleExcelImpl
//import com.pharbers.util.excel.phHandleExcelTrait
//import org.scalatest.FunSuite
//
///**
//  * Created by clock on 17-9-7.
//  */
//class PfizerUnitSuite extends FunSuite {
//
//    test("read 201611+CPA.xlsx") {
//        val a = "010021".toLong
//        println(a)
//        val file = "/home/clock/Downloads/TEST CPA.xlsx"
//        println(s"处理 CPA 数据 : $getDate")
//        val startTime = new Date()
//
//        //3.月份筛选
//        implicit val filterFun = getYMFilter(_: Map[String, String], "201611")
//
//        //4.填补缺失值
//        val setDefaultMap = getDefault
//
//        //5. 生成最小产品单位
//        implicit val postFun = getGenerateMin1Fun
//
//        _data_connection.getCollection("c0").drop
//        val parser: phHandleExcelTrait = new phHandleExcelImpl
//        parser.readToDB(file, "c0", defaultValueArg = setDefaultMap).foreach(println)
////        _data_connection.getCollection("c0").drop
//
//        println(s"读入 CPA 数据 到数据库 complete,耗时=" + getConsumingTime(startTime))
//    }
//
//    test("read 201611+GYCX.xlsx") {
//        val file = "/home/clock/workSpace/blackMirror/dependence/program/generatePanel/file/Client/GYCX/201611 GYCX.xlsx"
//        println(s"读入 GYCX 数据 到数据库 $getDate")
//        val startTime = new Date()
//        //2.列名修改
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
//
//        //3.月份筛选
//        implicit val filterFun = getYMFilter(_: Map[String, String], "201611")
//
//        //4.填补缺失值
//        val setDefaultMap = getDefault
//
//        //5. 生成最小产品单位
//        implicit val postFun = getGenerateMin1Fun
//
//        _data_connection.getCollection("g0").drop
//        val parser: phHandleExcelTrait = new phHandleExcelImpl
//        parser.readToDB(file, "g0", fieldArg = setFieldMap, defaultValueArg = setDefaultMap).foreach(println)
//        _data_connection.getCollection("g0").drop
//
//        println(s"读入 GYCX 数据 到数据库 complete,耗时=" + getConsumingTime(startTime))
//    }
//
//    test("read 产品标准化+vs+IMS_Pfizer_6市场others.xlsx ") {
//        println(s"读入 产品标准化+vs+IMS_Pfizer_6市场others.xls 数据 到数据库 $getDate")
//        val startTime = new Date()
//
//        val file = "/home/clock/Downloads/产品标准化+vs+IMS_Pfizer_6市场others.xlsx"
//        val parser: phHandleExcelTrait = new phHandleExcelImpl
//
//        val result = parser.readToList(file).map{x =>
//            Map("min1" -> x("min1"), "min1_标准" -> x("min1_标准"), "通用名" -> x("通用名"))
//        }.distinct
//
//        val coll = _data_connection.getCollection("m1")
//        coll.drop
//        result.foreach{x =>
//            val builder = MongoDBObject.newBuilder
//            x.keys.foreach { k =>
//                builder += k -> x(k)
//            }
//            coll += builder.result()
//        }
//        result.foreach(println)
////        _data_connection.getCollection("m1").drop
//
//        println(s"读入 产品标准化+vs+IMS_Pfizer_6市场others.xls 数据 到数据库 complete,耗时=" + getConsumingTime(startTime))
//    }
//
//    test("read universe_inf.xlsx") {
//        println(s"读入 universe_inf 数据 到数据库 $getDate")
//        val startTime = new Date()
//
//        val file = "/home/clock/Downloads/universe_inf.xlsx"
//        val parser: phHandleExcelTrait = new phHandleExcelImpl
//
//        //列名修改
//        val setFieldMap = Map(
//            "样本医院编码" -> "ID",
//            "PHA医院名称" -> "HOSP_NAME",
//            "PHA ID" -> "HOSP_ID",
//            "市场" -> "DOI"
//        )
//
//        //筛选
//        implicit val filterFun: Map[String, String] => Boolean = { tr =>
//            tr.get("If Panel_All") match {
//                case None => false
//                case Some(s) if s == "1" => true
//                case _ => false
//            }
//        }
//
//        //新建列
//        implicit val postFun: (Map[String,String]) => Option[Map[String, String]] = {tr =>
//            Some(tr ++ Map("DOIE" -> tr("DOI")))
//        }
//
//        _data_connection.getCollection("hos00").drop
//        parser.readToDB(file, "hos00", fieldArg = setFieldMap).foreach(println)
//
//        println(s"读入 universe_inf 数据 到数据库 complete,耗时=" + getConsumingTime(startTime))
//    }
//
//    test("read 按辉瑞采购清单中的通用名划分6市场others.xlsx ") {
//        import phHandleExcelImpl._
//        val file_local = "/home/clock/Downloads/按辉瑞采购清单中的通用名划分6市场others.xlsx"
//        val parser: phHandleExcelTrait = new phHandleExcelImpl
//        for(i <- 1 to parser.getCount(file_local))
//            parser.readToDB(file_local, "b0", i).foreach(println)
////        _data_connection.getCollection("b0").drop
//    }
//
//
//
//    private def getDate = {
//        val currentTime = new Date()
//        val formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//        formatter.format(currentTime)
//    }
//
//    private def getConsumingTime(start: Date): String ={
//        val consumingTime = new Date(new Date().getTime - start.getTime)
//        val formatter = new SimpleDateFormat("mm:ss")
//        formatter.format(consumingTime)
//    }
//
//    private val getYMFilter: (Map[String, String], String) => Boolean = { (tr, ym) =>
//        val year = ym.substring(0, 4)
//        val month = ym.substring(4)
//        val fy = {
//            tr.get("YEAR") match {
//                case None => false
//                case Some(s) if s == year => true
//                case _ => false
//            }
//        }
//        val fm = {
//            tr.get("MONTH") match {
//                case None => false
//                case Some(s) if s == month => true
//                case _ => false
//            }
//        }
//
//        if(fy && fm) true else false
//    }
//
//    private def getDefault = Map(
//        "PRODUCT_NAME" -> "$MOLE_NAME",
//        "VALUE" -> "0",
//        "STANDARD_UNIT" -> "0"
//    )
//
//    private val getGenerateMin1Fun: (Map[String,String]) => Option[Map[String, String]] = {tr =>
//        Some(tr ++ Map("min1" -> (tr("PRODUCT_NAME") + tr("APP2_COD") + tr("PACK_DES") + tr("PACK_NUMBER") + tr("CORP_NAME"))))
//    }
//}
