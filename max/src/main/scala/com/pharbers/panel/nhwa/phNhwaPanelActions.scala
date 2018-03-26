package com.pharbers.panel.nhwa

import scala.collection.immutable.Map
import com.pharbers.panel.panel_path_obj

case class phNhwaPanelActions(args: Map[String, List[String]])
                             (override val ym: List[String],
                              override val mkt: String) extends phNhwaPanelActionsTrait {

    lazy val uid: String = args.getOrElse("uid", throw new Exception("no find uid arg")).head
    override lazy val company: String = args.getOrElse("company", throw new Exception("no find company arg")).head
    lazy val cpa: String = args.getOrElse("cpas", throw new Exception("no find CPAs arg")).head
    lazy val gycx: String = args.getOrElse("gycxs", throw new Exception("no find GYCXs arg")).head

    override lazy val cpa_file = panel_path_obj.p_base_path + company + panel_path_obj.p_source_dir + cpa

    override lazy val product_match_file = panel_path_obj.p_base_path + company + panel_path_obj.p_product_match_file
    override lazy val markets_match_file = panel_path_obj.p_base_path + company + panel_path_obj.p_markets_match_file
    override lazy val universe_file = panel_path_obj.p_base_path + company + panel_path_obj.p_universe_file.replace("##market##", mkt)
    override lazy val not_published_hosp_file = panel_path_obj.p_base_path + company + panel_path_obj.p_not_published_hosp_file
    override lazy val full_hosp_file = panel_path_obj.p_base_path + company + panel_path_obj.p_fill_hos_data_file

    override lazy val cache_location = panel_path_obj.p_base_path + panel_path_obj.p_cache_dir
}