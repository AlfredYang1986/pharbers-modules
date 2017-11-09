package com.pharbers.bson.writer

import java.io.{FileNotFoundException, IOException, RandomAccessFile}
import org.bson._

/**
  * Created by jeorch on 17-10-26.
  */
case class phBsonWriter(override val file_path : String) extends PharbersBsonWriterTrait {
    def apply(map : Map[String, Any], file_local: String): phBsonWriter = {
        val obj = new phBsonWriter(file_local)
        obj.writeBsonFile(obj.map2bson(map))
        obj
    }
}

trait PharbersBsonWriterTrait {

    val file_path : String

    lazy val raf = new RandomAccessFile(file_path, "rw")

    def map2bson(map : Map[String, Any]) : BSONObject = {
        val bson : BSONObject = new BasicBSONObject()
        map.foreach(x => bson.put(x._1, x._2))
        bson
    }

    def map2arrByte(map : Map[String, Any]) : Array[Byte] = {
        val bson : BSONObject = new BasicBSONObject()
        val encode : BSONEncoder = new BasicBSONEncoder()
        map.foreach(x => bson.put(x._1, x._2))
        encode.encode(bson)
    }

    def writeBsonListFile(bson_list : List[BSONObject]) : Unit = {

        try {
            val encode : BSONEncoder = new BasicBSONEncoder()
            var lst : List[Byte] = Nil
            val tmp = bson_list.map( x => lst = encode.encode(x).toList ::: lst )
            //            val out : OutputStream = new BufferedOutputStream(new FileOutputStream(file), 16)
            // raf.write(lst.toArray[Byte], raf.length().toInt, lst.toArray[Byte].length)
            raf.seek(raf.length)
            raf.write(lst.toArray[Byte])
        } catch {
            case ex: FileNotFoundException => ex.printStackTrace()
            case ex: SecurityException => ex.printStackTrace()
            case ex: IOException => ex.printStackTrace()
            case _ => ???
        }
    }

    def writeBsonFile(bson : BSONObject) : Unit = {

        try {
            val encode : BSONEncoder = new BasicBSONEncoder()
            val byte_arr = encode.encode(bson)
            //            val out : OutputStream = new BufferedOutputStream(new FileOutputStream(file), 16)
            // raf.write(byte_arr, raf.length().toInt, byte_arr.length)
            raf.seek(raf.length)
            raf.write(byte_arr)
        } catch {
            case ex: FileNotFoundException => ex.printStackTrace()
            case ex: SecurityException => ex.printStackTrace()
            case ex: IOException => ex.printStackTrace()
            case _ => ???
        }
    }

    def close = raf.close()
}
