package com.pharbers.common.datatype.string

object PhStringOpt {
	def removeSpace(str: String): String = str.replaceAll("\\s", "")
}