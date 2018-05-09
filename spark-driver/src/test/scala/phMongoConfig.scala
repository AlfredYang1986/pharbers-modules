import com.pharbers.baseModules.PharbersInjectModule

/**
  * Created by clock on 18-2-26.
  */
trait phMongoConfig extends PharbersInjectModule {
    override val id: String = "mongo-config"
    override val configPath: String = "pharbers_config/mongo-config.xml"
    override val md = "host" :: "port" ::  Nil

    protected val mongodbHost: String = config.mc.find(p => p._1 == "host").get._2.toString
    protected val mongodbPort: String = config.mc.find(p => p._1 == "port").get._2.toString
}
