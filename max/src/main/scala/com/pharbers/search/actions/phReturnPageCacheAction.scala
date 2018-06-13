package com.pharbers.search.actions

import com.pharbers.sercuity.Sercurity
import com.pharbers.driver.PhRedisDriver
import com.pharbers.pactions.actionbase._

/**
  * Created by jeorch on 18-5-9.
  */
object phReturnPageCacheAction {
    def apply(args: pActionArgs = NULLArgs): pActionTrait = new phReturnPageCacheAction(args)
}

class phReturnPageCacheAction(override val defaultArgs: pActionArgs) extends pActionTrait {
    override val name: String = "return_page_cache_action"

    override def perform(pr: pActionArgs): pActionArgs = {

        val user = defaultArgs.asInstanceOf[MapArgs].get("user").asInstanceOf[StringArgs].get
        val company = defaultArgs.asInstanceOf[MapArgs].get("company").asInstanceOf[StringArgs].get
        val ym_condition = defaultArgs.asInstanceOf[MapArgs].get("ym_condition").asInstanceOf[StringArgs].get
        val mkt = defaultArgs.asInstanceOf[MapArgs].get("mkt").asInstanceOf[StringArgs].get
        val pageIndex = defaultArgs.asInstanceOf[MapArgs].get("pi").asInstanceOf[StringArgs].get.toInt
        val pageSize = defaultArgs.asInstanceOf[MapArgs].get("ps").asInstanceOf[StringArgs].get.toInt

        val pageCacheKey = Sercurity.md5Hash(user + company + ym_condition + mkt + pageIndex + pageSize)
        val cachedPageData = new PhRedisDriver().getListAllValue(pageCacheKey)

        ListArgs(cachedPageData.map(StringArgs))
    }
}
