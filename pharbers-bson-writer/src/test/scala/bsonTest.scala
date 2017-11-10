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

   // test("分段式写入bson Test") {
   //     println("分段式写入bson Test")
   //     val bfm = bsonFlushMemory("/home/jeorch/jeorch/test/26102017/test2.bson")
   //     val bson_obj1 : BSONObject = new BasicBSONObject()
   //     val bson_obj2 : BSONObject = new BasicBSONObject()
   //     val bson_obj3 : BSONObject = new BasicBSONObject()
   //     bson_obj1.put("k1", 123)
   //     bson_obj2.put("k2", 45.6)
   //     bson_obj3.put("k3", "v3")
   //     bfm.appendObject(bson_obj1)
   //     bfm.appendObject(bson_obj2)
   //     bfm.appendObject(bson_obj3)
   //     bfm.close
   // }
}
