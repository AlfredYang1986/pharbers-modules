//package spark

import com.pharbers.panelgen.actionContainer.NhwaYMActions
import com.pharbers.sparkSteam.paction.actionbase.NULLArgs
import org.scalatest.FunSuite

class SPFileFormatSuit extends FunSuite {

    object tf extends NhwaYMActions

    test("Spark File Convert") {
        import tf.progressFunc
        tf.perform(NULLArgs)
    }
}
