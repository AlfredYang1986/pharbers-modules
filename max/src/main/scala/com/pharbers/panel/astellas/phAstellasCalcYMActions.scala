package com.pharbers.panel.astellas

import scala.collection.immutable.Map
import com.pharbers.panel.panel_path_obj

case class phAstellasCalcYMActions(args: Map[String, List[String]]) extends phAstellasCalcYMActionsTrait {
    override val name = ""
    lazy val uid: String = args.getOrElse("uid", throw new Exception("no find uid arg")).head
    lazy val company: String = args.getOrElse("company", throw new Exception("no find company arg")).head
    lazy val cpa: String = args.getOrElse("cpas", throw new Exception("no find CPAs arg")).head
    lazy val gycx: String = args.getOrElse("gycxs", throw new Exception("no find GYCXs arg")).head

    override lazy val cpa_file: String = panel_path_obj.p_base_path + company + panel_path_obj.p_source_dir + cpa
    override lazy val gycx_file: String = panel_path_obj.p_base_path + company + panel_path_obj.p_source_dir + gycx
}