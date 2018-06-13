package com.pharbers.builder.maintenance

import java.io.File
import java.util.Date

import com.mongodb.casbah.Imports._
import com.pharbers.builder.phMarketTable.{phMarketManager, phMarketDBTrait, phReflectCheck}
import com.pharbers.common.algorithm.max_path_obj
import com.pharbers.dbManagerTrait.dbInstanceManager
import org.apache.commons.io.FileUtils
import org.bson.types.ObjectId
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

/**
  * Created by jeorch on 18-6-6.
  */
trait MaintenanceUpdateTrait  extends phReflectCheck with phMarketDBTrait with phMarketManager {

    def replaceMatchFile(data: JsValue): (Option[Map[String, JsValue]], Option[JsValue]) = {

        val company_id = (data \ "condition" \ "maintenance" \ "company_id").asOpt[String].get
        val origin_file_key = (data \ "condition" \ "origin_file" \ "file_key").asOpt[String].get
        val origin_file_name = (data \ "condition" \ "origin_file" \ "file_name").asOpt[String].get
        val current_file_uuid = (data \ "condition" \ "current_file" \ "file_uuid").asOpt[String].get                         //上传后服务器上新匹配文件的名字
        val current_file_name = (data \ "condition" \ "current_file" \ "file_name").asOpt[String].getOrElse(origin_file_name) //上传的新匹配文件名

        val db = new dbInstanceManager{}.queryDBInstance("calc").get
        val query: DBObject = DBObject("company" -> company_id)
        val output: DBObject => Map[String, JsValue] = obj =>
            obj.map{ x =>
                if(x._1 == "_id") x._1 -> toJson(obj.getAs[ObjectId](x._1).getOrElse(ObjectId.get()).toString)
                else x._1 -> toJson(obj.getAs[String](x._1).getOrElse(""))
            }.toMap

        def updateFunc(map: Map[String, JsValue]): DBObject = {
            val builder = MongoDBObject.newBuilder
            map.foreach(x =>
                if(x._1 == "_id") builder += x._1 -> new ObjectId(x._2.asOpt[String].get)
                else if(x._1 == origin_file_key) builder += x._1 -> x._2.asOpt[String].get.replaceAll(origin_file_name, current_file_name)
                else builder += x._1 -> x._2.asOpt[String].get
            )
            builder.result()
        }

        db.queryMultipleObject(query, "market_table", "company")(output) foreach  {x =>
            db.updateObject(updateFunc(x),"market_table","_id")
        }

        //replace match_file and backup
        val origin_file_path = getCompanyTables(company_id).find(x => x(origin_file_key).contains(origin_file_name))
            .getOrElse(throw new Exception(s"Error! Replace {origin_file_key:$origin_file_key, origin_file_name:$origin_file_name}"))(origin_file_key)
        val origin_file = new File(max_path_obj.p_matchFilePath + origin_file_path)
        val origin_file_bk = new File(max_path_obj.p_matchFilePath + s"bk/${new Date().getTime}_$origin_file_name")
        FileUtils.copyFile(origin_file, origin_file_bk)
        origin_file.delete()
        val upload_file = new File(max_path_obj.p_clientPath + current_file_uuid)
        val current_file = new File(max_path_obj.p_matchFilePath + origin_file_path.replaceAll(origin_file_name, current_file_name))
        FileUtils.copyFile(upload_file, current_file)

        (Some(Map("file_key" -> toJson(origin_file_key), "file_name" -> toJson(current_file_name))), None)
    }

}
