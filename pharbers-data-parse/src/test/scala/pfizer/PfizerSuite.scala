package pfizer

import akka.actor.ActorSystem
import com.pharbers.pfizer.impl.phPfizerHandleImpl
import com.pharbers.pfizer.phPfizerHandleTrait
import org.scalatest.FunSuite

/**
  * Created by clock on 17-9-7.
  */
class PfizerSuite extends FunSuite {

    test("pfizer create panel file") {
        val cpa_file_local = "/home/clock/Downloads/201611+CPA.xlsx"
        implicit val system = ActorSystem("phDataParse")
        val gycx_file_local = "/home/clock/Downloads/201611+GYCX.xlsx"

        val args: Map[String, List[String]] = Map(
            "company" -> List("空的，鹏哥不告诉我,哈哈,开玩笑的,他也不知道"),
            "ym" -> List("201611"),
            "cpas" -> List(cpa_file_local),
            "gycxs" -> List(gycx_file_local)
        )

        val phfizerParse: phPfizerHandleTrait = new phPfizerHandleImpl()
        val result = phfizerParse.generatePanelFile(args)

        println(result)
    }
}
