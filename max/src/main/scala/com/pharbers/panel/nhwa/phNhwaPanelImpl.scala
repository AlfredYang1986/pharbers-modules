package com.pharbers.panel.nhwa

import com.pharbers.panel.panel_path_obj
import com.pharbers.spark.driver.phSparkDriver

class phNhwaPanelImpl(company: String, cache_dir: String)(implicit sd: phSparkDriver) extends phNhwaPanelImplTrait {
    override val sparkDriver = sd
    override val cpa_file: String = cache_dir + "/cpa"
    override val gycx_file: String = ""

    override val product_match_file: String = cache_dir + "/product_match_file"
    override val universe_file: String = cache_dir + "/universe_file"
    override val markets_match_file: String = cache_dir + "/markets_match_file"
    override val not_arrival_hosp_file: String = cache_dir + "/not_arrival_hosp_file"
    override val not_published_hosp_file: String = cache_dir + "/not_published_hosp_file"
    override val full_hosp_file: String = cache_dir + "/full_hosp_file"

    override val output_location: String = panel_path_obj.p_base_path + company + panel_path_obj.p_output_dir
}
