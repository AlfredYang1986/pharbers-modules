package com.pharbers.calc

import java.io.File
import java.util.{Base64, UUID}

import com.pharbers.builder.Builderimpl
import com.pharbers.common.algorithm.max_path_obj
import com.pharbers.dbManagerTrait.dbInstanceManager
import com.pharbers.delivery.util.mongo_config_obj
import com.pharbers.driver.PhRedisDriver
import com.pharbers.sercuity.Sercurity
import com.pharbers.spark.phSparkDriver
import org.apache.spark.sql.DataFrame

/**
  * Created by jeorch on 18-5-15.
  */
case class phMaxScheduleJob(user: String) {

    private val maxSingleDayJobsKey = Sercurity.md5Hash("Pharbers")
    private val delimiter = 31.toChar.toString


    def rdd2mongo: Int = {
        val rd = new PhRedisDriver()
        val sd = phSparkDriver()
        val admin = rd.getString("MaxAdmin")
        var rdd2mongoJobsCount = 0
        if(user == admin){
            rd.getSetAllValue(maxSingleDayJobsKey).foreach(singleJobKey => {
                val maxName = rd.getMapValue(singleJobKey, "max_result_name")
                val resultLocation = max_path_obj.p_maxPath + maxName
                val singleJobDF = sd.csv2RDD(resultLocation, delimiter)
                singleJobDF.write.format("com.mongodb.spark.sql.DefaultSource").mode("overwrite")
                    .option("uri", s"mongodb://${mongo_config_obj.mongodbHost}:${mongo_config_obj.mongodbPort}/")
                    .option("database", mongo_config_obj.databaseName)
                    .option("collection", singleJobKey)
                    .save()
                rdd2mongoJobsCount += 1
            })
            rd.delete(maxSingleDayJobsKey)
        } else rdd2mongoJobsCount = -1
        rdd2mongoJobsCount
    }

    def mongo2rdd: Int = {
        val rd = new PhRedisDriver()
        val sd = phSparkDriver()
        val admin = rd.getString("MaxAdmin")
        var mongo2rddJobsCount = 0
        if(user == admin){
            val db = new dbInstanceManager{}.queryDBInstance("data").get
            db.getOneDBAllCollectionNames.foreach(singleJobKey => {
                val maxName = UUID.randomUUID().toString
                val resultLocation = max_path_obj.p_maxPath + maxName
                delTempFile(new File(resultLocation))
                val singleJobDF = sd.mongo2RDD(mongo_config_obj.mongodbHost, mongo_config_obj.mongodbPort, mongo_config_obj.databaseName, singleJobKey).toDF()
                singleJobDF.drop("_id").write
                    .format("csv")
                    .option("header", value = true)
                    .option("delimiter", delimiter)
                    .option("codec", "org.apache.hadoop.io.compress.GzipCodec")
                    .save(resultLocation)

                //TODO:加载mongo数据到rdd时，做聚合处理，为了渲染结果检查中的历史数据.
                //TODO:暂时存到redis中，之后直接存到数据库的另一张表中【PS：有了一层缓存了没必要再多一层】
                aggregationData2Redis(singleJobDF, singleJobKey, maxName)

                mongo2rddJobsCount += 1
            })
        } else mongo2rddJobsCount = -1
        mongo2rddJobsCount
    }

    //TODO:需要把聚合数据存到mongo对应的表中
    def aggregationData2Redis(dataFrame: DataFrame, singleJobKey: String, maxName: String) = {
        val rd = new PhRedisDriver()
        val singleJobInfoArr = new String(Base64.getDecoder.decode(singleJobKey)).split("#")
        val company = singleJobInfoArr(0)
        val ym = singleJobInfoArr(1)
        val mkt = singleJobInfoArr(2)

        val condition = Builderimpl().getSubsidiary(company).get.map(x => s"Product like '%$x%'").mkString(" OR ") //获得所有子公司
        val maxDF_filter_company = dataFrame.filter(condition)

        val max_sales_city_lst_key = Sercurity.md5Hash(company + ym + mkt + "max_sales_city_lst_key")
        val max_sales_prov_lst_key = Sercurity.md5Hash(company + ym + mkt + "max_sales_prov_lst_key")
        val company_sales_city_lst_key = Sercurity.md5Hash(company + ym + mkt + "company_sales_city_lst_key")
        val company_sales_prov_lst_key = Sercurity.md5Hash(company + ym + mkt + "company_sales_prov_lst_key")

        rd.delete(
            max_sales_city_lst_key,
            max_sales_prov_lst_key,
            company_sales_city_lst_key,
            company_sales_prov_lst_key
        )

        rd.addMap(singleJobKey, "max_result_name", maxName)
        rd.addMap(singleJobKey, "user", user)
        rd.addMap(singleJobKey, "company", company)
        rd.addMap(singleJobKey, "ym", ym)
        rd.addMap(singleJobKey, "mkt", mkt)

        val max_sales = dataFrame.agg(Map("f_sales" -> "sum")).take(1)(0).toString().split('[').last.split(']').head.toDouble
        val max_sales_city_lst = dataFrame.groupBy("City").agg(Map("f_sales" -> "sum")).sort("sum(f_sales)")
            .collect().map(x => x.toString())
        val max_sales_prov_lst = dataFrame.groupBy("Province").agg(Map("f_sales" -> "sum")).sort("sum(f_sales)")
            .collect().map(x => x.toString())

        val max_company_sales = if (maxDF_filter_company.count() == 0) 0.0
        else maxDF_filter_company.agg(Map("f_sales" -> "sum")).take(1)(0).toString().split('[').last.split(']').head.toDouble

        val company_sales_city_lst = maxDF_filter_company.groupBy("City").agg(Map("f_sales" -> "sum")).sort("sum(f_sales)")
            .collect().map(x => x.toString())
        val company_sales_prov_lst = maxDF_filter_company.groupBy("Province").agg(Map("f_sales" -> "sum")).sort("sum(f_sales)")
            .collect().map(x => x.toString())

        rd.addMap(singleJobKey, "max_sales", max_sales)
        rd.addMap(singleJobKey, "max_company_sales", max_company_sales)
        rd.addListLeft(max_sales_city_lst_key, max_sales_city_lst:_*)
        rd.addListLeft(max_sales_prov_lst_key, max_sales_prov_lst:_*)
        rd.addListLeft(company_sales_city_lst_key, company_sales_city_lst:_*)
        rd.addListLeft(company_sales_prov_lst_key, company_sales_prov_lst:_*)

        rd.expire(singleJobKey, 60*60*24)
        rd.expire(max_sales_city_lst_key, 60*60*24)
        rd.expire(max_sales_prov_lst_key, 60*60*24)
        rd.expire(company_sales_city_lst_key, 60*60*24)
        rd.expire(company_sales_prov_lst_key, 60*60*24)

    }

    def delTempFile(fileName: File): Unit = {
        if (fileName.isDirectory) {
            fileName.listFiles().toList match {
                case Nil => fileName.delete()
                case lstFile => lstFile.foreach(delTempFile); fileName.delete()
            }
        } else {
            fileName.delete()
        }
    }

}
