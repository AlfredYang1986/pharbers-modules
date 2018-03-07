package com.pharbers.panel.util.excel

import scala.collection.immutable.Map
import com.pharbers.panel.util.common.phFileInfoTrait

/**
  * Created by clock on 18-2-27.
  */
case class phExcelFileInfo(file_local: String,
                           sheetId: Int = 1,
                           fieldArg: Map[String, String] = Map(),
                           defaultValueArg: Map[String, String] = Map(),
                           sheetName: String = "") extends phFileInfoTrait