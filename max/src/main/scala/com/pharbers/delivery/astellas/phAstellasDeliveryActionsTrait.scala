package com.pharbers.delivery.astellas

import com.pharbers.delivery.astellas.format.{phAstellasHospitalMatchFormat, phAstellasMedicineMatchFormat}
import com.pharbers.pactions.jobs.sequenceJobWithMap
import com.pharbers.pactions.actionbase.pActionTrait
import com.pharbers.pactions.generalactions.{jarPreloadAction, saveMapResultAction, xlsxReadingAction}

/**
  * Created by jeorch on 18-3-28.
  */
trait phAstellasDeliveryActionsTrait extends sequenceJobWithMap {
    val company: String
    val dbName: String
    val lstColl: List[String]
    val historyFile: String
    val destPath: String
    val hospitalMatchFile: String
    val medicineMatchFile: String

    val readHospitalMatchAction = xlsxReadingAction[phAstellasHospitalMatchFormat](hospitalMatchFile, "hospital_match_key")
    val readMedicineMatchAction = xlsxReadingAction[phAstellasMedicineMatchFormat](medicineMatchFile, "medicine_match_key")

    override val actions: List[pActionTrait] = jarPreloadAction() ::
        phReadAstellasHistoryDataAction(historyFile, "history_rdd_key") ::
        phReadMongo2RDDAction(company, dbName, lstColl, "mongo_rdd_key") ::
        readMedicineMatchAction :: readHospitalMatchAction ::
        phAstellasDeliveryAction() :: saveMapResultAction[String]("deliveryResult", destPath, ".txt") ::
        Nil
}
