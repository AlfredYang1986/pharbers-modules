package com.pharbers.calc

import com.pharbers.pactions.actionbase.pActionTrait
import com.pharbers.pactions.generalactions.{csv2DFAction, jarPreloadAction}
import com.pharbers.pactions.jobs.sequenceJobWithMap
import com.pharbers.panel.panel_path_obj

/**
  * Created by jeorch on 18-4-9.
  */
trait phMaxActionsTrait extends sequenceJobWithMap {

    val uid: String
    val panel: String

    val result_path = panel_path_obj.p_resultPath

    override val actions: List[pActionTrait] =
        jarPreloadAction() ::
        csv2DFAction(result_path + panel)::
        phMaxGroupAction(panel,"max_group") ::
        Nil

}
