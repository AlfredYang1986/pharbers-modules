package com.pharbers.delivery.nhwa

import com.pharbers.pactions.actionContainer.pActionContainer
import com.pharbers.pactions.actionbase.pActionTrait

/**
  * Created by jeorch on 18-3-28.
  */
trait phNhwaDeliveryActionsTrait extends pActionContainer {
    val company: String
    val dbName: String
    val lstColl: List[String]
    val destPath: String
    override val actions: List[pActionTrait] = phNhwaDeliveryAction(company, dbName, lstColl, destPath) ::
        Nil
}
