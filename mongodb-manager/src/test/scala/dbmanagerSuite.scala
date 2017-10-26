
import com.pharbers.dbManagerTrait.dbInstanceManager
import org.scalatest.FunSuite

/**
  * Created by alfredyang on 07/07/2017.
  */
class dbmanagerSuite extends FunSuite {

    test("db manager test") {
        object tmp extends dbInstanceManager

        tmp.config
        println(tmp.config)
        println(tmp.connections)
        assert(tmp.queryDBInstance("cli") != None)
    }
}