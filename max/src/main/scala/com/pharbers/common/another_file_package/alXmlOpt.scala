package com.pharbers.common.another_file_package

/**
  * Created by qianpeng on 2017/5/11.
  */
object alXmlOpt {
	def apply(path: String): alXmlOpt = new alXmlOpt(path)
}

class alXmlOpt(path: String) {

	def xmlFindV(key: String) = {
		(xml.XML.loadFile(path) \ key).map(_.text)
	}
}
