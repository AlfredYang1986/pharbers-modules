//package pfizer
//
//import org.scalatest.FunSuite
//import scala.collection.immutable.Map
//import com.pharbers.panel.phPanelFilePath
//import com.pharbers.memory.pages.pageMemory
//import com.pharbers.panel.pfizer.phPfizerHandle
//
///**
//  * Created by clock on 17-9-7.
//  */
//class PfizerUnitSuite extends FunSuite with phPanelFilePath {
//    val args: Map[String, List[String]] = Map(
//        "company" -> List("company"),
//        "user" -> List("user"),
//        "cpas" -> List("1705 CPA.xlsx"),
//        "gycxs" -> List("1705 GYC.xlsx")
//    )
//
//    test("load CPA") {
//        println(phPfizerHandle(args).loadCPA)
//    }
//
//    test("test fill hos lst") {
//        val fill_hos_lst = phPfizerHandle(args).fill_hos_lst(5)
//        println(s"num = ${fill_hos_lst.length}")
//        fill_hos_lst.foreach(println)
//    }
//
//    test("load GYCX") {
//        println(phPfizerHandle(args).loadGYCX)
//    }
//
//    test("test calcYM") {
//        println(phPfizerHandle(args).calcYM)
//    }
//
//    test("load m1") {
//        phPfizerHandle(args).load_m1.foreach(println)
//    }
//
//    test("load hos0") {
//        phPfizerHandle(args).load_hos00("INF").foreach(println)
//    }
//
//    test("load b0") {
//        phPfizerHandle(args).load_b0("INF").foreach(println)
//    }
//
//    test("test inner join") {
//        val b0 = phPfizerHandle(args).load_b0("INF")
//        val m1 = phPfizerHandle(args).load_m1
//        phPfizerHandle(args).innerJoin(b0.toStream, m1.toStream, "CPA反馈通用名", "通用名").foreach(println)
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
//        println(new phPfizerHandleImpl(args).getPanelFile("201705" :: Nil))
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
