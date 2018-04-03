//package util
//
//import java.io.FileOutputStream
//import java.net.URI
//
//import org.scalatest.FunSuite
//import com.pharbers.panel.phPanelFilePath
//import com.pharbers.spark.phSparkDriver
//import org.apache.hadoop.conf.Configuration
//import org.apache.hadoop.fs.{FileSystem, FileUtil, Path}
//import org.apache.hadoop.io.IOUtils
//
///**
//  * Created by clock on 17-9-7.
//  */
//class HDFSHeadleSuite extends FunSuite with phPanelFilePath {
//    test("test ls") {
//        val conf = new Configuration()
//        val file_path = "hdfs://192.168.100.174:12138"
//        val hdfs: FileSystem = FileSystem.get(URI.create(file_path), conf)
//
//        val fs = hdfs.listStatus(new Path("/user/jeorch"))
//        val listPath = FileUtil.stat2Paths(fs)
//
//        println("----------------------------------------")
//        for (p <- listPath) {
//            println(p)
//        }
//        println("----------------------------------------")
//    }
//
//    test("test create file") {
//        val conf = new Configuration()
//        System.setProperty("HADOOP_USER_NAME", "jeorch")
//        val file_path = "hdfs://192.168.100.174:54188"
//        val hdfs: FileSystem = FileSystem.get(URI.create(file_path), conf)
//
//        hdfs.create(new Path("/user/jeorch/test22222.txt"))
//    }
//
//    test("test delete file") {
//        val conf = new Configuration()
//        val file_path = "hdfs://192.168.100.174:54188"
//        val hdfs: FileSystem = FileSystem.get(URI.create(file_path), conf)
//
//        hdfs.delete(new Path("/user/jeorch/test2.txt"), true)
//    }
//
//    test("spark write csv in hdfs => Failure") {
//        val conf = new Configuration()
//        conf.set("fs.defaultFS", "hdfs://192.168.100.174:54188/user/clock/test222.csv")
//        val serverPath = "hdfs://192.168.100.174:54188/user/clock/test222.csv"
//        val hdfsfile = new Path(serverPath)
//        val hdfs = FileSystem.get(URI.create(serverPath), conf)
//        val out = hdfs.create(hdfsfile)
//
//        out.write(1)
//
//        out.flush()
//        out.close()
//
//    }
//
//    test("spark read csv in hdfs") {
//        val file_path = "hdfs://192.168.100.174:54188/user/jeorch/csv.csv"
//        val rdd = phSparkDriver().csv2RDD(file_path, delimiter = 31.toChar.toString, true)
//        rdd.show(false)
//    }
//
//    test("test download hdfs file to local") {
//        val hdfs_file = "hdfs://192.168.100.174:12138/user/jeorch/匹配表.csv"
//        val local_dir = "./config/aaaa.txt"
//
//        val conf = new Configuration()
//        val fs = FileSystem.get(URI.create(hdfs_file), conf)
//        val fsdi = fs.open(new Path(hdfs_file))
//        val output = new FileOutputStream(local_dir)
//        IOUtils.copyBytes(fsdi,output,4096,true)
//    }
//}
