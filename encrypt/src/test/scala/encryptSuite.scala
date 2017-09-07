
import org.scalatest.FunSuite
import com.pharbers.encrypt.RSA.RSAEncryptTrait


/**
  * Created by alfredyang on 07/07/2017.
  */
class encryptSuite extends FunSuite {

    test("pharbers encrypt test") {
        object te extends RSAEncryptTrait
        assert(te.db_key == "encrypt_config")
        assert(te.project == "Dongda")
        assert(!te.privateKey.isEmpty)
        assert(!te.publicKey.isEmpty)
    }
}