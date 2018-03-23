//package spark

import com.pharbers.sparkSteam.paction.actionbase.NULLArgs
import com.pharbers.sparkSteam.panelgen.NhwaPanelActions
import org.scalatest.FunSuite

class SPFileFormatSuit extends FunSuite {

    object tf extends NhwaPanelActions

    test("Spark File Convert") {
        import tf.progressFunc
        tf.perform(NULLArgs)
    }
}
