package com.pharbers.panel.astellas

import java.util.UUID
import scala.collection.immutable.Map
import com.pharbers.panel.panel_path_obj

case class phAstellasPanelActions(args: Map[String, List[String]])
                                 (override val ym: List[String],
                                  override val mkt: String) extends phAstellasPanelActionsTrait {

    lazy val uid: String = args.getOrElse("uid", throw new Exception("no find uid arg")).head
    override lazy val company: String = args.getOrElse("company", throw new Exception("no find company arg")).head
    lazy val cpa: String = args.getOrElse("cpas", throw new Exception("no find CPAs arg")).head
    lazy val gycx: String = args.getOrElse("gycxs", throw new Exception("no find GYCXs arg")).head

    override lazy val cpa_file = panel_path_obj.p_base_path + company + panel_path_obj.p_source_dir + cpa
    override lazy val gycx_file = panel_path_obj.p_base_path + company + panel_path_obj.p_source_dir + gycx

    override lazy val product_match_file = panel_path_obj.p_base_path + company// + panel_path_obj.p_product_match_file
    override lazy val markets_match_file = panel_path_obj.p_base_path + company// + panel_path_obj.p_markets_match_file
    override lazy val hospital_file = panel_path_obj.p_base_path + company + "/医院名称编码等级三源互匹20180314.xlsx"
    override lazy val universe_file = panel_path_obj.p_base_path + company// + panel_path_obj.p_universe_file.replace("##market##", mkt)

    lazy val panelID = UUID.randomUUID.toString
    override lazy val panel_file = panel_path_obj.p_base_path + company + panel_path_obj.p_output_dir + panelID
}