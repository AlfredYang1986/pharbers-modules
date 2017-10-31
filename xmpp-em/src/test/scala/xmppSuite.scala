
import org.scalatest.FunSuite
import com.pharbers.xmpp.em.EMImpl

/**
  * Created by alfredyang on 07/07/2017.
  */
class xmppSuite extends FunSuite {

    test("pharbers module config path") {
        object tmp extends EMImpl
        assert(tmp.emn.app_key == "blackmirror#dongda")
        assert(tmp.emn.org_name == "blackmirror")
        assert(tmp.emn.app_name == "dongda")
        assert(!tmp.emn.client_id.isEmpty)
        assert(!tmp.emn.client_secret.isEmpty)
        assert(tmp.emn.notification_account == "dongda_master")
        assert(tmp.emn.notification_password == "PassW0rd")
        assert(!tmp.emn.em_host.isEmpty)
    }
}