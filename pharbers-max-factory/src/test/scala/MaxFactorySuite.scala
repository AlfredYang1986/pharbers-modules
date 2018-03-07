import com.pharbers.spark.driver.phSparkDriver
import org.scalatest.FunSuite

/**
  * Created by jeorch on 18-3-6.
  */
class MaxFactorySuite extends FunSuite {

    val driver =  phSparkDriver()

    test ("connect spark test") {
        val file_path = "/home/jeorch/work/max/files/NHWA/max 結果.csv"
        val rdd = driver.csv2RDD(file_path)
        rdd.select("Panel_ID", "Date", "City")
        rdd.show(false)
    }

}
