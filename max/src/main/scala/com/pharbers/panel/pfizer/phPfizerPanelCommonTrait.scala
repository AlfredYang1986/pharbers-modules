package com.pharbers.panel.pfizer

/**
  * Created by jeorch on 18-4-28.
  */
trait phPfizerPanelCommonTrait {
    def getFatherMarket(mkt: String):String = mkt match {
        case "PAIN_C" => "PAIN_other"
        case "HTN2" => "HTN"
        case "AI_D" => "AI_W"
        case "ZYVOX" => "AI_R_other"
        case _ => mkt
    }
}
