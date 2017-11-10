package com.pharbers.bson.writer.fop

import java.io.{File, OutputStream, RandomAccessFile}
import java.nio.{ByteBuffer, MappedByteBuffer}
import java.nio.channels.FileChannel
import java.util

import org.bson.io.OutputBuffer
import org.bson._

trait BsonFlushTrait extends OutputBuffer {

    val path : String
    val bufferSize : Int

    var position = 0

    lazy val buffer : Array[Byte] = new Array[Byte](bufferSize)
    lazy val raf : RandomAccessFile = new RandomAccessFile(new File(path), "rw")
    lazy val fc: FileChannel = raf.getChannel

    lazy val encode : BSONEncoder = {
        val result = new BasicBSONEncoder()
        result.set(this)
        result
    }

    def closeFlush = {
        flushToPharbersFile
        encode.done()
        fc.close()
        raf.close()
    }

    def appendBsonObject(o : BSONObject) : Unit = encode.putObject(o)

    def flushToPharbersFile = {
        lazy val mem: MappedByteBuffer = fc.map(FileChannel.MapMode.READ_WRITE, raf.length(), position)
        mem.put(buffer, 0, position)

        position = 0
    }

    override def write(b: Array[Byte]): Unit = {
        this.ensureOpen()
        this.write(b, 0, b.length)
    }

    override def writeBytes(bytes: Array[Byte], offset: Int, length: Int): Unit = {
        this.ensureOpen()
        this.ensure(length)
        System.arraycopy(bytes, offset, this.buffer, this.position, length)
        this.position += length
    }

    override def writeByte(value: Int): Unit = {
        this.ensureOpen()
        this.ensure(1)
        this.buffer({
            this.position += 1; this.position - 1
        }) = (255 & value).toByte
    }

    override protected def write(absolutePosition: Int, value: Int) : Unit = {
        this.ensureOpen()
        if (absolutePosition < 0) throw new IllegalArgumentException(s"position must be >= 0 but was ${absolutePosition}")
        else if (absolutePosition > this.position - 1) throw new IllegalArgumentException(s"position must be <= ${this.position - 1} but was ${absolutePosition}")
        else this.buffer(absolutePosition) = (255 & value).toByte
    }

    override def getPosition : Int = {
        this.ensureOpen()
        this.position
    }

    override def getSize: Int = {
        this.ensureOpen()
        this.position
    }

    override def pipe(out: OutputStream) : Int = {
        this.ensureOpen()
        out.write(this.buffer, 0, this.position)
        this.position
    }

    override def truncateToPosition(newPosition: Int) : Unit = {
        this.ensureOpen()
        if (newPosition <= this.position && newPosition >= 0) this.position = newPosition
        else throw new IllegalArgumentException
    }

    override def getByteBuffers : util.List[ByteBuf] = {
        this.ensureOpen()
        util.Arrays.asList(new ByteBufNIO(ByteBuffer.wrap(this.buffer, 0, this.position).duplicate))
    }

    override def close() : Unit = closeFlush

    private def ensureOpen() : Unit = {
        if (this.buffer == null) throw new IllegalStateException("The output is closed")
    }

    private def ensure(more : Int): Unit = {
        if (this.position + more > this.buffer.length)
            flushToPharbersFile
    }
}
