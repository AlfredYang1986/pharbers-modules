package com.pharbers.bson.writer

import java.io.{FileNotFoundException, IOException, RandomAccessFile}
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

import org.bson._

/**
  * Created by jeorch on 17-10-26.
  */
case class phBsonWriter(override val file_path : String) extends PharbersBsonWriterTrait

trait PharbersBsonWriterTrait {

    val file_path : String

    val bufferSize = 31457280
    lazy val buf = new Array[Byte](bufferSize)
    lazy val raf = new RandomAccessFile(file_path, "rw")
    lazy val fc : FileChannel = raf.getChannel
    var position = 0

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

    def writeBsonFile2(bson : BSONObject) : Unit = {

        val encode : BSONEncoder = new BasicBSONEncoder()
        val byte_arr = encode.encode(bson)
        val len = byte_arr.length
        if (position + len > bufferSize) {
            flush
            writeBsonFile2(bson)
        }
        byte_arr.copyToArray(buf, 0, len)
        position += len
    }

    def flush = {
        lazy val mem: MappedByteBuffer = fc.map(FileChannel.MapMode.READ_WRITE, raf.length(), position)
        mem.put(buf, 0, position)
        position = 0
    }

    def close = {
        fc.close()
        raf.close()
    }

    def closeFlush = {
        flush
        fc.close()
        raf.close()
    }
}
