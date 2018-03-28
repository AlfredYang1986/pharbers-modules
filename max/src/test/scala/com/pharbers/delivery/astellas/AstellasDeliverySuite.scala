package com.pharbers.panel.nhwa

import java.io.File

import com.pharbers.panel.astellas.phAstellasHandle
import org.scalatest.FunSuite
import org.specs2.io.FileName

/**
  * Created by jeorch on 18-3-28.
  */
class AstellasDeliverySuite extends FunSuite {
    val company_name = "8ee0ca24796f9b7f284d931650edbd4b"
    val dbName = "Max_Cores"
    val coll1 = "8ee0ca24796f9b7f284d931650edbd4ba2c5e077-4231-4e74-9c11-8f17391c0ab0"
    val destPath = "/mnt/config/FileBase/AstellasDelivery"

    val args: Map[String, List[String]] = Map(
        "company" -> List(company_name),
        "dbName" -> List(dbName),
        "lstColl" -> List(coll1),
        "destPath" -> List(destPath)
    )
    test("test astellas delivery"){
        val f = new File(destPath)
        delTempFile(f)
        phAstellasHandle(args).generateDeliveryFile
    }

    def delTempFile(fileName: File): Unit ={
        if(fileName.isDirectory) {
            val lstFile = fileName.listFiles().toList
            lstFile.length match {
                case 0 => fileName.delete()
                case _ => lstFile.foreach(delTempFile); fileName.delete()
            }
        } else {
            fileName.delete()
        }
    }
}
