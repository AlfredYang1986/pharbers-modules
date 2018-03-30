package com.pharbers.delivery.astellas

import com.mongodb.spark.rdd.MongoRDD
import com.pharbers.paction.actionbase._
import com.pharbers.panel.format.input.writable.astellas.{phAstellasHospitalMatchWritable, phAstellasProductMatchWritable}
import org.bson.Document

/**
  * Created by jeorch on 18-3-29.
  */
object phAstellasDeliveryAction {
    def apply(): pActionTrait = new phAstellasDeliveryAction
}

class phAstellasDeliveryAction extends pActionTrait {

    override val defaultArgs: pActionArgs = NULLArgs

    override implicit def progressFunc(progress: Double, flag: String): Unit = {}

    override def perform(pr: pActionArgs)(implicit f: (Double, String) => Unit): pActionArgs = {

        val dataMap = pr.asInstanceOf[MapArgs].get
        val hospital_match = dataMap("hospital_match_key").asInstanceOf[RDDArgs[phAstellasHospitalMatchWritable]].get
        val medicine_match = dataMap("medicine_match_key").asInstanceOf[RDDArgs[phAstellasProductMatchWritable]].get
        val mongo_rdd = dataMap("mongo_rdd_key").asInstanceOf[RDDArgs[Document]].get

        val mongoRDDTuple1 = mongo_rdd.map{ row =>
            row.get("Panel_ID").toString -> row
//            Map(
//                "Panel_ID" -> row.get("Panel_ID"),
//                "Date" -> row.get("Date"),
//                "Product" -> row.get("Product"),
//                "Sales" -> row.get("Sales"),
//                "Units" -> row.get("Units"),
//                "factor" -> row.get("factor"),
//                "f_sales" -> row.get("f_sales"),
//                "f_units" -> row.get("f_units"),
//                "City" -> row.get("City")
//            )
        }
        val hospitalRDDTuple = hospital_match.map(x => x.getRowKey("PHA_ID") -> x)
        val joinedHospitalRDD = mongoRDDTuple1.leftOuterJoin(hospitalRDDTuple)

        val medicineRDDTuple = medicine_match.map(x => x.getRowKey("min2") -> (x.getRowKey("STANDARD_MOLE_NAME"), x.getRowKey("min2"))).distinct()
        val mongoRDDTuple2 = mongo_rdd.map(row => row.get("Product").toString -> row)
        val joinedMedicineRDD = mongoRDDTuple2.leftOuterJoin(medicineRDDTuple)

//        val test = joinedMedicineRDD
//        test.take(10).foreach(x => println(x))
//        println(test.count())
//        StringArgs(test)
        defaultArgs
    }
}