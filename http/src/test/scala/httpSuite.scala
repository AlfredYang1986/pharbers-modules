//import com.pharbers.http.HTTP
//import org.scalatest.FunSuite
//
///**
//  * Created by jeorch on 17-12-12.
//  */
//class httpSuite extends FunSuite {
//
//    test("pharbers http get") {
//        println(s"test1")
//        val test1 = HTTP("https://a1.easemob.com/blackmirror/dongda/users/jeorch/disconnect").header("Accept" -> "application/json", "Content-Type" -> "application/json", "Authorization" -> ("Bearer " + "YWMtl6lhQLfCEeey5E-W2VUufwAAAAAAAAAAAAAAAAAAAAED67NwOfgR5qDpL0JFi_X1AgMAAAFfSBpSIABPGgAMhdX7pCYtgbkE4OUzMV9X4LvBGa3jBIwitIDV62f7zw")).get
//        println(s"test1=>${test1}")
//        val result = (test1 \ "data" \ "result").asOpt[Boolean].getOrElse(throw new Exception("http get failed"))
//        println(s"result=>${result}")
//    }
//
//}
