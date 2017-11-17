import org.scalatest.FunSuite
import com.pharbers.bson.writer._
import org.bson.{BSONObject, BasicBSONObject}
/**
  * Created by jeorch on 17-10-26.
  */
class bsonTest extends FunSuite{

//    test("pharbers module bson writer test") {
//        println("Start1 pharbers module bson writer test")
//        phBsonWriterByMap("/home/jeorch/jeorch/test/26102017/test.bson").writeBsonFile()
//    }

//    test("pharbers module bson_list writer test") {
//        println("Start1 pharbers module bson writer test")
//        val lst = 1 to 10 map (i => phBsonWriterByMap().map2bson(Map("key" -> "value")))
//        phBsonWriterByMap().writeBsonListFile(lst.toList, "/home/jeorch/jeorch/test/26102017/test1.bson")
//    }

//    test("分段式写入bson Test") {
//        println("分段式写入bson Test")
//
//        val path = "/home/jeorch/jeorch/test/26102017/test.bson"
//
//        val bfm = bsonFlushMemory(path)
//        val bw = phBsonWriter(path)
//
//        val m1 = Map("ID" -> "efb9157eb3bdec6e5a850dc11430a7ed",
//            "city_Index" -> "f10cc82ff6416c248c9a19d771930f97",
//            "f_units" -> 0.0,
//            "Panel_ID" -> "PHA0023730",
//            "hosp_Index" -> "a242fd1dc1aae92b1bf2aaeec75ceba9",
//            "Market" -> "INF",
//            "f_sales" -> 0.0,
//            "Provice" -> "广西",
//            "City" -> "柳州市",
//            "Date" -> 1493568000000L,
//            "Product" -> "关平片剂10MG10河北万岁药业有限公司",
//            "prov_Index" -> "1b9fd47f4329a3f06883be7e82e6a07d")
//
////        1 to 50000 foreach (i => bfm.appendObject(bw.map2bson(m1)))
//        1 to 50000 foreach (i => bw.writeBsonFile2(bw.map2bson(m1)))
//        bw.closeFlush
//        bfm.close
//    }
}
