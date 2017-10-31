package com.pharbers.bson.writer

import java.io.{FileNotFoundException, IOException, RandomAccessFile}
import org.bson._

/**
  * Created by jeorch on 17-10-26.
  */
case class phBsonWriterByMap() extends PharbersBsonWriterTrait {
    def apply(map : Map[String, Any], file_local: String): phBsonWriterByMap = {
        val obj = new phBsonWriterByMap()
        obj.writeBsonFile(obj.map2bson(map), file_local)
        obj
    }
}

trait PharbersBsonWriterTrait {

    def map2bson(map : Map[String, Any]) : BSONObject = {
        val bson : BSONObject = new BasicBSONObject()
        map.foreach(x => bson.put(x._1, x._2))
        bson
    }

    def writeBsonFile(bson : BSONObject, file_path: String) : Unit = {

        try {
            val encode : BSONEncoder = new BasicBSONEncoder()
            val byte_arr = encode.encode(bson)
            //            val out : OutputStream = new BufferedOutputStream(new FileOutputStream(file), 16)
            val out = new RandomAccessFile(file_path, "rw")
            out.seek(out.length)
            out.write(byte_arr)
            out.close()
        } catch {
            case ex: FileNotFoundException => ex.printStackTrace()
            case ex: SecurityException => ex.printStackTrace()
            case ex: IOException => ex.printStackTrace()
            case _ => ???
        }
    }
}
