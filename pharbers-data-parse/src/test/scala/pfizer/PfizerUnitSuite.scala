//package pfizer
//
//import java.nio.CharBuffer
//import java.nio.charset.Charset
//
//import com.pharbers.memory.pages.fop.{fileStorage, pageStorage}
//import com.pharbers.memory.pages.pageMemory
//import com.pharbers.panel.pfizer.impl.phPfizerHandleImpl
//import com.pharbers.panel.pfizer.panel_file_path
//import com.pharbers.panel.util.csv.{phFileWriteStorageImpl, phHandleCsvImpl}
//import org.scalatest.FunSuite
//import play.api.libs.json.JsValue
//
//import scala.collection.immutable.Map
//import scalaz.syntax.std.all
//
//
///**
//  * Created by clock on 17-9-7.
//  */
//class PfizerUnitSuite extends FunSuite with panel_file_path {
//    val args: Map[String, List[String]] = Map(
//        "company" -> List("company"),
//        "user" -> List("user"),
//        "cpas" -> List("config/company/Client/CPA/1705 CPA.xlsx"),
//        "gycxs" -> List("config/company/Client/GYCX/1705 GYC.xlsx")
//    )
//
//    test("load CPA") {
//        println(new phPfizerHandleImpl(args).loadCPA)
//    }
//
//    test("load GYCX") {
//        println(new phPfizerHandleImpl(args).loadGYCX)
//    }
//
//    test("test calcYM") {
//        println(new phPfizerHandleImpl(args).calcYM)
//    }
//
//    test("page memory") {
//        val page = pageMemory("config/company/Cache/test.cache")
//        println(s"t.size = ${page.allLength}")
//        println(s"t.pageCount = ${page.pageCount}")
//
//        page.pageData(1).zipWithIndex.foreach { x =>
//            println(s"${x._2} : ${x._1}")
//        }
//    }
//
//    test("load m1") {
//        new phPfizerHandleImpl(args).load_m1.foreach(println)
//    }
//
//    test("load hos0") {
//        new phPfizerHandleImpl(args).load_hos00.foreach(println)
//    }
//
//    test("load b0") {
//        new phPfizerHandleImpl(args).load_b0("INF").foreach(println)
//    }
//
//    test("test inner join") {
//        val b0 = new phPfizerHandleImpl(args).load_b0("INF")
//        val m1 = new phPfizerHandleImpl(args).load_m1
//        new phPfizerHandleImpl(args).innerJoin(b0.toStream, m1.toStream, "CPA反馈通用名", "通用名").foreach(println)
//    }
//
//    test("test generate panel file") {
//        def getResult(data: JsValue) ={
//            data.as[Map[String, JsValue]].map{ x =>
//                x._1 -> x._2.as[Map[String, JsValue]].map{y =>
//                    y._1 -> y._2.as[List[String]]
//                }
//            }
//        }
//
//        val data = new phPfizerHandleImpl(args).getPanelFile("201705" :: Nil)
//        val result = getResult(data)
//        println(result)
//
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
//        val test_file = base_file + "test3" :: Nil
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
//        println("result = " + phHandleCsvImpl().sortInsert(insert_data(1), test_file, distinct_source, sameLineFun))
//    }
//}
