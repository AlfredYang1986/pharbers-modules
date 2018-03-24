package com.pharbers.paction.actionbase

trait pActionTrait {
    var name : String = ""
    val defaultArgs : pActionArgs
    def perform(pr : pActionArgs)(implicit f : (Double, String) => Unit) : pActionArgs
    implicit def progressFunc(progress : Double, flag : String) : Unit
}
