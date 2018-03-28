package com.pharbers.delivery.astellas

import com.pharbers.paction.actionContainer.pActionContainer
import com.pharbers.paction.actionbase.pActionTrait
import com.pharbers.paction.funcTrait.saveCurrenResultTrait

/**
  * Created by jeorch on 18-3-28.
  */
trait phAstellasDeliveryActionsTrait extends pActionContainer {
    val company: String
    val dbName: String
    val lstColl: List[String]
    val destPath: String
    override val actions: List[pActionTrait] =
        phDeliveryReadMongoAction(company, dbName, lstColl) ::
            saveCurrenResultTrait(destPath) :: Nil
}
