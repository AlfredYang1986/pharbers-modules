package com.pharbers.panel2.util.excel

import com.pharbers.panel2.util.phData
import scala.collection.immutable.Map

/**
  * Created by clock on 17-10-23.
  */
case class phExcelData(file_local: String,
                       sheetId: Int = 1,
                       fieldArg: Map[String, String] = Map(),
                       defaultValueArg: Map[String, String] = Map(),
                       sheetName: String = "") extends phData