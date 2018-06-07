package com.pharbers.builder.maintenance

import java.io.File
import java.util.Date

import com.pharbers.builder.{CheckTrait, MarketTable}
import com.pharbers.common.algorithm.max_path_obj
import org.apache.commons.io.FileUtils
import play.api.libs.json.JsValue
import play.api.libs.json.Json.toJson

import scala.util.parsing.json.{JSONArray, JSONObject}

/**
  * Created by jeorch on 18-6-6.
  */
trait MaintenanceUpdateTrait  extends CheckTrait with MarketTable {

    def replaceMatchFile(jv: JsValue): (Option[Map[String, JsValue]], Option[JsValue]) = {

        val company_id = (jv \ "condition" \ "maintenance" \ "company_id").asOpt[String].get
        val origin_file_key = (jv \ "condition" \ "origin_file" \ "file_key").asOpt[String].get
        val origin_file_name = (jv \ "condition" \ "origin_file" \ "file_name").asOpt[String].get
        val current_file_uuid = (jv \ "condition" \ "current_file" \ "file_uuid").asOpt[String].get                         //上传后服务器上新匹配文件的名字
        val current_file_name = (jv \ "condition" \ "current_file" \ "file_name").asOpt[String].getOrElse(origin_file_name) //上传的新匹配文件名

        //update market_table
        val newMarketTable = getCompanyTables(company_id).map(x =>
            if(x(origin_file_key).contains(origin_file_name)) JSONObject.apply(x ++ Map(origin_file_key -> x(origin_file_key).replaceAll(origin_file_name, current_file_name)))
            else JSONObject.apply(x)
        ) ::: getNotCompanyTables(company_id)

        //replace market_table and backup
        val origin_market_table_file = new File("pharbers_config/market_table.json")
        val origin_market_table_file_bk = new File(s"pharbers_config/${new Date().getTime}_market_table.json")
        FileUtils.copyFile(origin_market_table_file, origin_market_table_file_bk)
        origin_market_table_file.delete()
        FileUtils.writeStringToFile(new File("pharbers_config/market_table.json"), JSONArray.apply(newMarketTable).toString().replace("\\", ""))

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
