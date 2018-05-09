package com.pharbers.pactions.actionbase

trait pActionTrait extends java.io.Serializable  {
    val name : String
    val defaultArgs : pActionArgs
    def perform(pr : pActionArgs)(implicit f : (Double, String) => Unit) : pActionArgs

    val progressFactor: Int = 100
    implicit def progressFunc(progress : Double, flag : String) : Unit

    require(0 <= progressFactor && progressFactor <= 100, "progress factor is error")
}
