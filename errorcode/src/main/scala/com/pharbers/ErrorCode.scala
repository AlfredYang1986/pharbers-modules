package com.pharbers

import play.api.libs.json.Json
import play.api.libs.json.Json._
import play.api.libs.json.JsValue

object ErrorCode {
  	case class ErrorNode(name : String, code : Int, message : String)

  	private def xls : List[ErrorNode] = List(
		new ErrorNode("input error", -1, "输入的参数有错误"),

		new ErrorNode("get primary key error", -101, "获取主健健值失败"),
		new ErrorNode("primary key error", -102, "主健重复创建或者主键出错"),
		new ErrorNode("data not exist", -103, "数据不存在"),
		new ErrorNode("data duplicate", -104, "搜索结果不唯一，用query multiple搜索"),

		new ErrorNode("user push error", -201, "用户创建失败"),
        new ErrorNode("user already exist", -202, "用户已经存在"),
		new ErrorNode("user not exist", -203, "用户不存在"),
		new ErrorNode("profile query input error", -204, "搜索用户没有提供用户ID"),
		new ErrorNode("profile multi query input error", -205, "搜索多用户没有提供用户ID列表"),
		new ErrorNode("profile update input error", -206, "修改用户信息修改错误"),
		new ErrorNode("profile update no right", -207, "只有自己能修改自己的详细信息"),

        new ErrorNode("reg push error", -301, "由于上传数据原因，验证码发送失败"),
        new ErrorNode("reg phone or code error", -302, "电话号码或者验证码出错"),

		new ErrorNode("push service input error", -401, "添加服务时参数错误"),
		new ErrorNode("search service input error", -402, "搜索服务时参数错误, 需要服务ID"),
		new ErrorNode("service result error", -403, "服务信息数据出错"),
		new ErrorNode("only can push own service", -404, "自己只能发自己的服务"),
		new ErrorNode("only service provider can push service", -405, "只有成为服务者才能发布信息"),
		new ErrorNode("pop service input error", -406, "删除服务时需要服务ID和发布者ID"),
		new ErrorNode("only can pop own service", -407, "自己只能删除自己的服务"),
		new ErrorNode("only can update own service", -408, "自己只能修改自己的服务"),
		new ErrorNode("service not exist", -409, "服务不存在"),

		new ErrorNode("push order input error", -501, "添加订单时输入参数错误"),
        new ErrorNode("order output error", -502, "订单数据返回解析错误"),
        new ErrorNode("order detail condition error", -503, "缺少订单id"),
        new ErrorNode("query multi order condition error", -504, "缺少订单id列表"),
        new ErrorNode("order not exist", -505, "查询的订单不存在"),
        new ErrorNode("order notify error", -506, "订单状态修改发送提示失败"),
        new ErrorNode("no right modify order", -507, "没有权限修改订单相关信息"),

        new ErrorNode("tm input error", -601, "添加时间管理参数错误"),
        new ErrorNode("tm output error", -602, "时间管理数据返回解析错误"),

        new ErrorNode("collection input error", -701, "收藏输入参数错误"),
		new ErrorNode("collection output error", -702, "收藏输出参数错误"),

		new ErrorNode("dongda selected input error", -801, "咚哒严选输入参数错误"),
        new ErrorNode("dongda selected output error", -802, "咚哒严选输出参数错误"),

		new ErrorNode("category input error", -1001, "咚哒分类输入参数错误"),
		new ErrorNode("category output error", -1002, "咚哒分类输出参数错误"),
		new ErrorNode("search index input error", -1003, "咚哒分类输入参数错误"),
		new ErrorNode("search index output error", -1004, "咚哒分类输出参数错误"),

		new ErrorNode("no db connection", -901, "没找到数据库链接"),
		new ErrorNode("db prase error", -902, "数据库结构发现错误"),
		new ErrorNode("no encrypt impl", -903, "权限加密方式不清晰或者Token不存在"),
		new ErrorNode("token parse error", -904, "token数据解析出现错误"),
		new ErrorNode("token expired", -905, "token过期"),
		new ErrorNode("db aggregation error", -906, "数据Map Reduce操作发生错误"),
		new ErrorNode("notification module not exist", -907, "用户提醒服务模块不存在"),
		new ErrorNode("akka module not exist", -908, "系统并发分发模块不存在"),
		new ErrorNode("merge result error", -909, "合并多结果失败"),
		new ErrorNode("actor system get error", -910, "获取系统Actor System失败"),

		new ErrorNode("unknown error", -999, "unknown error")
	)

	def getErrorCodeByName(name : String) : Int = (xls.find(x => x.name == name)) match {
		case Some(y) => y.code
		case None => -9999
	}

	def getErrorMessageByName(name : String) : String = (xls.find(x => x.name == name)) match {
		case Some(y) => y.message
		case None => "unknow error"
	}

	def errorToJson(name : String) : JsValue =
		Json.toJson(Map("status" -> toJson("error"), "error" ->
			toJson(Map("code" -> toJson(this.getErrorCodeByName(name)), "message" -> toJson(this.getErrorMessageByName(name))))))
}