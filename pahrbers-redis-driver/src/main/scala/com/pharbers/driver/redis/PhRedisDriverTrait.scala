package com.pharbers.driver.redis

import com.pharbers.baseModules.PharbersInjectModule
import com.pharbers.driver.redis.hash.hashDriverTrait
import com.pharbers.driver.redis.list.listDriverTrait
import com.pharbers.driver.redis.set.setDriverTrait
import com.redis.RedisClient

/**
  * Created by jeorch on 17-11-17.
  */
trait PhRedisDriverTrait extends PharbersInjectModule {
    override val id: String = "redis-config"
    override val configPath: String = "pharbers_config/redis_config.xml"
    override val md = "host":: "port" :: Nil

    val host = config.mc.find(p => p._1 == "host").get._2.toString
    val port = config.mc.find(p => p._1 == "port").get._2.toString.toInt

    case class listDriverImpl(override val conn : RedisClient) extends listDriverTrait
    case class setDriverImpl(override val conn : RedisClient) extends setDriverTrait
    case class hashDriverImpl(override val conn : RedisClient) extends hashDriverTrait

    lazy val common_driver = new RedisClient(host, port)
    lazy val phListDriver = listDriverImpl(common_driver)
    lazy val phSetDriver = setDriverImpl(common_driver)
    lazy val phHashDriver = hashDriverImpl(common_driver)

    def del(key : Any, keys : Any*) : Unit = {
        common_driver.del(key, keys)
    }

    def flushall = common_driver.flushall

}

case class phRedisDriver() extends PhRedisDriverTrait


