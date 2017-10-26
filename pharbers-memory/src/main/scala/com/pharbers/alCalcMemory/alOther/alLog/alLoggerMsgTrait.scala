package com.pharbers.alCalcMemory.alOther.alLog

import org.slf4j.LoggerFactory

/**
  * Created by clock on 2017/10/16.
  */
trait alLoggerMsgTrait {
	def logger = LoggerFactory.getLogger(getClass)
}


