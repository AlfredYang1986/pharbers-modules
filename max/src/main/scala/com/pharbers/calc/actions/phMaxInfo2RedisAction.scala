package com.pharbers.calc.actions

import java.util.{Base64, UUID}

import com.pharbers.builder.phMarketTable.{Builderimpl, phMarketManager}
import com.pharbers.common.algorithm.max_path_obj
import com.pharbers.driver.PhRedisDriver
import com.pharbers.pactions.actionbase._
import com.pharbers.sercuity.Sercurity

object phMaxInfo2RedisAction {
    def apply(args: pActionArgs = NULLArgs): pActionTrait = new phMaxInfo2RedisAction(args)
}

class phMaxInfo2RedisAction(override val defaultArgs: pActionArgs) extends pActionTrait with phMarketManager{
    override val name: String = "phMaxInfo2RedisAction"
    override def perform(pr: pActionArgs): pActionArgs = {
        val rd = new PhRedisDriver()

        val ym = defaultArgs.asInstanceOf[MapArgs].get("ym").asInstanceOf[StringArgs].get
        val mkt = defaultArgs.asInstanceOf[MapArgs].get("mkt").asInstanceOf[StringArgs].get
        val maxName = pr.asInstanceOf[MapArgs].get("max_persistent_action").asInstanceOf[StringArgs].get
        val maxNameForSearch = UUID.randomUUID().toString
        val maxDF = pr.asInstanceOf[MapArgs].get("max_calc_action").asInstanceOf[DFArgs].get
        val company = defaultArgs.asInstanceOf[MapArgs].get("company").asInstanceOf[StringArgs].get
        val condition = getAllSubsidiary(company).map(x => s"Product like '%$x%'").mkString(" OR ") //获得所有子公司
        val maxDF_filter_company = maxDF.filter(condition)

        val singleJobKey = Base64.getEncoder.encodeToString((company +"#"+ ym +"#"+ mkt).getBytes())

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

        //TODO: Save按钮就下面三句话
        val maxSingleDayJobsKey = Sercurity.md5Hash("Pharbers")
        rd.addSet(maxSingleDayJobsKey, singleJobKey)
        rd.expire(maxSingleDayJobsKey, 60*60*24)

        val max_sales = maxDF.agg(Map("f_sales" -> "sum")).take(1)(0).toString().split('[').last.split(']').head.toDouble
        val max_sales_city_lst = maxDF.groupBy("City").agg(Map("f_sales" -> "sum")).sort("sum(f_sales)")
            .collect().map(x => x.toString())
        val max_sales_prov_lst = maxDF.groupBy("Province").agg(Map("f_sales" -> "sum")).sort("sum(f_sales)")
            .collect().map(x => x.toString())

        val max_company_sales = if (maxDF_filter_company.count() == 0) 0.0
                                else maxDF_filter_company.agg(Map("f_sales" -> "sum")).take(1)(0).toString().split('[').last.split(']').head.toDouble

        val company_sales_city_lst = maxDF_filter_company.groupBy("City").agg(Map("f_sales" -> "sum")).sort("sum(f_sales)")
            .collect().map(x => x.toString())
        val company_sales_prov_lst = maxDF_filter_company.groupBy("Province").agg(Map("f_sales" -> "sum")).sort("sum(f_sales)")
            .collect().map(x => x.toString())

        maxDF.groupBy("Date", "Province", "City", "MARKET", "Product")
            .agg(Map("f_sales"->"sum", "f_units"->"sum", "Panel_ID"->"first"))
            .write
            .format("csv")
            .option("header", value = true)
            .option("delimiter", 31.toChar.toString)
            .option("codec", "org.apache.hadoop.io.compress.GzipCodec")
            .save(max_path_obj.p_maxPath + maxNameForSearch)

        rd.addMap(singleJobKey, "max_result_name", maxName)
        rd.addMap(singleJobKey, "max_result_name_for_search", maxNameForSearch)
        rd.addMap(singleJobKey, "max_sales", max_sales)
        rd.addMap(singleJobKey, "max_company_sales", max_company_sales)
        rd.addListLeft(max_sales_city_lst_key, max_sales_city_lst:_*)
        rd.addListLeft(max_sales_prov_lst_key, max_sales_prov_lst:_*)
        rd.addListLeft(company_sales_city_lst_key, company_sales_city_lst:_*)
        rd.addListLeft(company_sales_prov_lst_key, company_sales_prov_lst:_*)

        rd.expire(max_sales_city_lst_key, 60*60*24)
        rd.expire(max_sales_prov_lst_key, 60*60*24)
        rd.expire(company_sales_city_lst_key, 60*60*24)
        rd.expire(company_sales_prov_lst_key, 60*60*24)

        StringArgs(singleJobKey)
    }
}