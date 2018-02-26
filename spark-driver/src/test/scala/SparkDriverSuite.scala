import com.mongodb.spark.MongoSpark
import com.mongodb.spark.config.ReadConfig
import com.pharbers.spark.driver.phSparkDriver
import org.scalatest.FunSuite

/**
  * Created by clock on 18-2-26.
  */
class SparkDriverSuite extends FunSuite with phMongoConfig{
    val sc = phSparkDriver().sc
    test("Test read mongo") {
        def users = {
            val readConfig = ReadConfig(Map(
                "spark.mongodb.input.uri" -> s"mongodb://$mongodbHost:$mongodbPort/",
                "spark.mongodb.input.database" -> "baby_time_test",
                "spark.mongodb.input.collection" -> "users",
                "readPreference.name" -> "secondaryPreferred")
            )
            MongoSpark.load(sc, readConfig = readConfig)
        }

        def services = {
            val readConfig = ReadConfig(Map(
                "spark.mongodb.input.uri" -> s"mongodb://$mongodbHost:$mongodbPort/",
                "spark.mongodb.input.database" -> "baby_time_test",
                "spark.mongodb.input.collection" -> "services",
                "readPreference.name" -> "secondaryPreferred"))
            MongoSpark.load(sc, readConfig = readConfig)
        }
        println(users)
        println(services)
    }
}
