package com.pharbers.sparkSteam.paction.actionbase

trait pActionTrait {
    val defaultArgs : pActionArgs
    def perform(args : pActionArgs)(implicit f : (Double, String) => Unit) : pActionArgs
    implicit def progressFunc(progress : Double, flag : String) : Unit
}
