
import com.pharbers.mongodbConnect._data_connection
import org.scalatest.FunSuite

/**
  * Created by alfredyang on 07/07/2017.
  */
class dbconnectSuite extends FunSuite {

    test("pharbers module config path") {
        println(_data_connection.configDir)
        assert(_data_connection.configDir.length != 0)
        assert(_data_connection.conn_name == "baby_time_test")
    }
}