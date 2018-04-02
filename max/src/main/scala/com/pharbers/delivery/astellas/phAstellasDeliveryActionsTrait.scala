package com.pharbers.delivery.astellas

import com.pharbers.delivery.astellas.format.{phAstellasHospitalMatchFormat, phAstellasMedicineMatchFormat}
import com.pharbers.paction.actionContainer.pMapActionContainer
import com.pharbers.paction.actionbase.pActionTrait
import com.pharbers.paction.funcTrait.{jarPreloadTrait, saveMapResultTrait, xlsxReadingTrait}

/**
  * Created by jeorch on 18-3-28.
  */
trait phAstellasDeliveryActionsTrait extends pMapActionContainer {
    val company: String
    val dbName: String
    val lstColl: List[String]
    val historyFile: String
    val destPath: String
    val hospitalMatchFile: String
    val medicineMatchFile: String

    val readHospitalMatchAction = xlsxReadingTrait[phAstellasHospitalMatchFormat](hospitalMatchFile, "hospital_match_key")
    val readMedicineMatchAction = xlsxReadingTrait[phAstellasMedicineMatchFormat](medicineMatchFile, "medicine_match_key")

    override val actions: List[pActionTrait] = jarPreloadTrait() ::
        phReadAstellasHistoryDataAction(historyFile, "history_rdd_key") ::
        phReadMongo2RDDAction(company, dbName, lstColl, "mongo_rdd_key") ::
        readMedicineMatchAction :: readHospitalMatchAction ::
        phAstellasDeliveryAction() :: saveMapResultTrait("deliveryResult", destPath) ::
        Nil
}
