package com.pharbers.message.common

import com.pharbers.baseModules.PharbersInjectModule

trait MessageTrait extends PharbersInjectModule {
	
	lazy val messageConf: Map[String, MessageTrait] =
		config.mc.find(p => p._1 == md.head)
			.get._2.asInstanceOf[List[(String, MessageTrait)]].toMap
	
	def queryMessageInstance(name: String): MessageTrait = messageConf.get(name).map(x => x).
		getOrElse(throw new Exception("not instance data"))
}
