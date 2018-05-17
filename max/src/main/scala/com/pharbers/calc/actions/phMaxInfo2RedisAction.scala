package com.pharbers.calc.actions

import com.pharbers.builder.Builderimpl
import com.pharbers.driver.PhRedisDriver
import com.pharbers.pactions.actionbase._
import com.pharbers.sercuity.Sercurity

object phMaxInfo2RedisAction {
    def apply(args: pActionArgs = NULLArgs): pActionTrait = new phMaxInfo2RedisAction(args)
}

class phMaxInfo2RedisAction(override val defaultArgs: pActionArgs) extends pActionTrait {
    override val name: String = "phMaxInfo2RedisAction"
    override def perform(pr: pActionArgs): pActionArgs = {
        val rd = new PhRedisDriver()

        val company = defaultArgs.asInstanceOf[MapArgs].get("company").asInstanceOf[StringArgs].get
        val user = defaultArgs.asInstanceOf[MapArgs].get("user").asInstanceOf[StringArgs].get
        val ym = defaultArgs.asInstanceOf[MapArgs].get("ym").asInstanceOf[StringArgs].get
        val mkt = defaultArgs.asInstanceOf[MapArgs].get("mkt").asInstanceOf[StringArgs].get
        val maxName = pr.asInstanceOf[MapArgs].get("max_persistent_action").asInstanceOf[StringArgs].get
        val maxDF = pr.asInstanceOf[MapArgs].get("max_calc_action").asInstanceOf[DFArgs].get
        val condition = Builderimpl().getSubsidiary(company).get.map(x => s"Prod_Name like '%$x%'").mkString(" OR ") //获得所有子公司
        val maxDF_filter_company = maxDF.filter(condition)

        val maxJobsKey = Sercurity.md5Hash("Pharbers")  //为了同步mongo数据到本地rdd
        val userJobsKey = Sercurity.md5Hash(user + company)
        val singleJobKey = Sercurity.md5Hash(user + company + ym + mkt)
        val max_sales_city_lst_key = Sercurity.md5Hash(user + company + ym + mkt + "max_sales_city_lst_key")
        val max_sales_prov_lst_key = Sercurity.md5Hash(user + company + ym + mkt + "max_sales_prov_lst_key")
        val company_sales_city_lst_key = Sercurity.md5Hash(user + company + ym + mkt + "company_sales_city_lst_key")
        val company_sales_prov_lst_key = Sercurity.md5Hash(user + company + ym + mkt + "company_sales_prov_lst_key")

        rd.delete(
            max_sales_city_lst_key,
            max_sales_prov_lst_key,
            company_sales_city_lst_key,
            company_sales_prov_lst_key
        )

        rd.addSet(maxJobsKey, singleJobKey)
        rd.addSet(userJobsKey, singleJobKey)
        rd.addMap(singleJobKey, "max_result_name", maxName)
        val max_sales = maxDF.agg(Map("f_sales" -> "sum")).take(1)(0).toString().split('[').last.split(']').head.toDouble
        val max_sales_city_lst = maxDF.groupBy("City").agg(Map("f_sales" -> "sum")).sort("sum(f_sales)")
            .collect().map(x => x.toString())
        val max_sales_prov_lst = maxDF.groupBy("Province").agg(Map("f_sales" -> "sum")).sort("sum(f_sales)")
            .collect().map(x => x.toString())
        val max_company_sales = maxDF_filter_company.agg(Map("f_sales" -> "sum")).take(1)(0).toString().split('[').last.split(']').head.toDouble
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

        StringArgs(singleJobKey)
    }
}