package com.pharbers.builder.maintenance

import java.io.File
import java.util.Date

import com.mongodb.casbah.Imports._
import com.pharbers.builder.phMarketTable.MongoDBPool._
import com.pharbers.builder.phMarketTable.phMarketManager
import com.pharbers.common.algorithm.max_path_obj
import org.apache.commons.io.FileUtils
import org.bson.types.ObjectId
import play.api.libs.json._
import play.api.libs.json.Json.toJson

/**
  * Created by jeorch on 18-6-6.
  */
trait MaintenanceUpdateTrait  extends phMarketManager {

    def replaceMatchFile(data: JsValue): (Option[Map[String, JsValue]], Option[JsValue]) = {

        val company_id = (data \ "condition" \ "maintenance" \ "company_id").asOpt[String].get
        val module_tag = (data \ "condition" \ "maintenance" \ "module_tag").asOpt[String].get
        val origin_file_key = (data \ "condition" \ "origin_file" \ "file_des").asOpt[String].get
        val current_file_uuid = (data \ "condition" \ "current_file" \ "file_uuid").asOpt[String].get                         //上传后服务器上新匹配文件的名字

        val db = MongoPool.queryDBInstance("market").get
        val query: DBObject = DBObject("company" -> company_id)

        var origin_file_path = ""
        var new_file_path = ""

        def updateFunc(map: Map[String, JsValue]): DBObject = {
            val moduleObj = map(module_tag).as[JsObject].value.toMap
            val updateFiles: Map[String, JsValue] = moduleObj("files").as[JsObject].value.toMap.map { x =>
                val tmp = x._2.as[JsObject].value.toMap
                if (tmp("des").as[JsString].value == origin_file_key) {
                    origin_file_path = tmp("path").as[JsString].value
                    new_file_path = origin_file_path.split(47.toChar.toString).dropRight(1).mkString(47.toChar.toString) +
                        47.toChar.toString + current_file_uuid
                    val updateItem = Map(
                        "name" -> tmp("name"),
                        "des" -> tmp("des"),
                        "path" -> toJson(new_file_path),
                        "update_date" -> toJson(new Date().getTime.toString)
                    )
                    (x._1, toJson(updateItem))
                } else (x._1, x._2)

            }
            val newModuleMap = moduleObj ++ Map("files" -> toJson(updateFiles))
            val newJsMap = map ++ Map(module_tag -> toJson(newModuleMap))
            DBObject.apply(toJson(newJsMap).toString()) += "_id" -> new ObjectId(map("_id").as[JsString].value)
        }

        queryMultipMarketTable(query) foreach ( x => updateMarketTable(updateFunc(x)) )

        //replace match_file to match_path
        val upload_file = new File(max_path_obj.p_clientPath + current_file_uuid)
        val current_file = new File(max_path_obj.p_matchFilePath + new_file_path)
        FileUtils.copyFile(upload_file, current_file)

        (Some(Map("file_des" -> toJson(origin_file_key), "file_name" -> toJson(current_file_uuid))), None)
    }
}
