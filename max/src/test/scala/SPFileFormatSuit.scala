//package spark

import com.pharbers.aqll.common.algorithm.ph_alg
import com.pharbers.paction.actionbase.NULLArgs
import com.pharbers.panel.NhwaPanelActions
import com.pharbers.panel.nhwa.NhwaYMActions
import org.scalatest.FunSuite

class SPFileFormatSuit extends FunSuite {

//    object tf extends NhwaYMActions
//    test("Spark File Convert") {
//        import tf.progressFunc
//        tf.perform(NULLArgs)
//    }
//
//    test("edit distance") {
//        val a = "aabbcc"
//        val b = "aabcc"
//
//        ph_alg.edit_distance(a, b) === 1
//        ph_alg.edit_distance("ATC编码", "ATC码") === 1
//    }

    object tf extends NhwaPanelActions
    test("Spark File Convert") {
        import tf.progressFunc
        tf.perform(NULLArgs)
    }
}
