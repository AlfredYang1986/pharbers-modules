package com.pharbers.pactions.actionbase

trait pActionTrait extends java.io.Serializable  {
    val name : String
    val defaultArgs : pActionArgs
    def perform(pr : pActionArgs)(implicit f : (Double, String) => Unit) : pActionArgs
    implicit def progressFunc(progress : Double, flag : String) : Unit
}
