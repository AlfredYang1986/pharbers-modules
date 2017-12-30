package com.pharbers.driver

import com.pharbers.driver.util.{redis_conn_instance, PhRedisTrait}

/**
  * Created by jeorch on 17-12-13.
  */
trait PhRedisDriverImpl extends PhRedisTrait {
    implicit val conn_instance : redis_conn_instance

    override def addString(key: Any, value: String): Unit ={
        conn_instance.getConnection.set(key, value)
    }

    override def addListLeft(key: Any, value: Any, values: Any*): Unit = {
        conn_instance.getConnection.lpush(key, value, values)
    }

    override def addListRight(key: Any, value: Any, values: Any*): Unit = {
        conn_instance.getConnection.rpush(key, value, values)
    }

    override def addMap(key: Any, values: Map[Any, Any]): Unit = {
        conn_instance.getConnection.hmset(key, values)
    }

    override def addMap(key: Any, mapK: Any, mapV: Any): Unit = {
        conn_instance.getConnection.hset(key, mapK, mapV)
    }

    override def addSet(key: Any, values: Set[Any]): Unit = {
        values.foreach(v => conn_instance.getConnection.sadd(key, v))
    }

    override def addSet(key: Any, value: Any, values: Any*): Unit = {
        conn_instance.getConnection.sadd(key, value, values)
    }

    override def delete(key: Any, keys: Any*): Long = {
        conn_instance.getConnection.del(key, keys) match {
            case None => throw new Exception("cannot get this value from redis")
            case value: Option[Long] => value.get
        }
    }

    override def getString(key: Any): String = {
        conn_instance.getConnection.get(key) match {
            case None => throw new Exception("cannot get this value from redis")
            case value: Option[String] => value.get
        }
    }

    override def getListAllValue(key: Any): List[String] = {
        conn_instance.getConnection.lrange(key, 0, -1) match {
            case None => throw new Exception("cannot get this value from redis")
            case value: Option[List[Option[String]]] => value.get.map(x => x.get)
        }
    }

    override def getListLeftValue(key: Any): String = {
        conn_instance.getConnection.lpop(key) match {
            case None => throw new Exception("cannot get this value from redis")
            case value: Option[String] => value.get
        }
    }

    override def getListRightValue(key: Any): String = {
        conn_instance.getConnection.rpop(key) match {
            case None => throw new Exception("cannot get this value from redis")
            case value: Option[String] => value.get
        }
    }

    override def getListSize(key: Any): Long = {
        conn_instance.getConnection.llen(key) match {
            case None => throw new Exception("cannot get this value from redis")
            case value: Option[Long] => value.get
        }
    }

    override def getMapAllValue(key: Any): Map[String, String] = {
        conn_instance.getConnection.hgetall1(key) match {
            case None => throw new Exception("cannot get this value from redis")
            case value: Option[Map[String, String]] => value.get
        }
    }

    override def getMapSize(key: Any): Long = {
        conn_instance.getConnection.hlen(key) match {
            case None => throw new Exception("cannot get this value from redis")
            case value: Option[Long] => value.get
        }
    }

    override def getMapValue(key: Any, mapK: Any): String = {
        conn_instance.getConnection.hget(key, mapK) match {
            case None => throw new Exception("cannot get this value from redis")
            case value: Option[String] => value.get
        }
    }

    override def getSetAllValue(key: Any): Set[String] = {
        conn_instance.getConnection.smembers(key) match {
            case None => throw new Exception("cannot get this value from redis")
            case value: Option[Set[Option[String]]] => value.get.map(x => x.get)
        }
    }

    override def getSetLastValue(key: Any): String = {
        conn_instance.getConnection.spop(key) match {
            case None => throw new Exception("cannot get this value from redis")
            case value: Option[String] => value.get
        }
    }

    override def getSetSize(key: Any): Long = {
        conn_instance.getConnection.scard(key) match {
            case None => throw new Exception("cannot get this value from redis")
            case value: Option[Long] => value.get
        }
    }

    override def exsits(key: Any): Boolean = {
        conn_instance.getConnection.exists(key)
    }

    override def expire(key: Any, ttl: Int): Unit = {
        conn_instance.getConnection.expire(key, ttl)
    }

    override def flush: Unit = {
        conn_instance.getConnection.flushall
    }

}