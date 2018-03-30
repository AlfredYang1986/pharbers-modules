package com.pharbers.delivery.astellas

import com.pharbers.delivery.astellas.format.phAstellasHospitalMatchFormat
import com.pharbers.paction.actionContainer.{pActionContainer, pMapActionContainer}
import com.pharbers.paction.actionbase.pActionTrait
import com.pharbers.paction.funcTrait.{jarPreloadTrait, saveCurrenResultTrait, xlsxReadingTrait}
import com.pharbers.panel.astellas.format.phAstellasProductMatchFormat

/**
  * Created by jeorch on 18-3-28.
  */
trait phAstellasDeliveryActionsTrait extends pMapActionContainer {
    val company: String
    val dbName: String
    val lstColl: List[String]
    val destPath: String
    val hospitalMatchFile: String
    val medicineMatchFile: String

    val readHospitalMatchAction = xlsxReadingTrait[phAstellasHospitalMatchFormat](hospitalMatchFile, "hospital_match_key")
    val readMedicineMatchAction = xlsxReadingTrait[phAstellasProductMatchFormat](medicineMatchFile, "medicine_match_key")

    override val actions: List[pActionTrait] = jarPreloadTrait() ::
        phReadMongo2RDDAction(company, dbName, lstColl, "mongo_rdd_key") ::
        readMedicineMatchAction :: readHospitalMatchAction ::
        phAstellasDeliveryAction() :: /*saveCurrenResultTrait(destPath) ::*/
        Nil
}
