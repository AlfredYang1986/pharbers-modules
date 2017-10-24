//    private def generatePanelFile(ym: String)
//                                 (implicit arg1: Map[String,(String, List[String])],
//                                  arg2: Map[String,List[Map[String,String]]]) = {
//        val c0 = arg1.getOrElse("c0", throw new Exception("参数错误"))
//        val g0 = arg1.getOrElse("g0", throw new Exception("参数错误"))
//        val m1 = arg2.getOrElse("m1", throw new Exception("参数错误"))
//        val hos00 = arg2.getOrElse("hos00", throw new Exception("参数错误"))
//
//        markets.map { market =>
//            val b0 = load_b0(market)
//            val m1_c = innerJoin(b0.toStream, m1.toStream, "CPA反馈通用名", "通用名").map(mergeMB(_))
//            val m1_g = innerJoin(b0.toStream, m1.toStream, "GYCX反馈通用名", "通用名").map(mergeMB(_))
//            val hosp_tab = getHospTab(hos00, market)
//            val hos0_hosp_id = hosp_tab.keys.toList
//
//            def filter_source(source: (String, List[String]),
//                              m1Arg: Stream[Map[String, String]],
//                              file_lst_arg: List[String] = Nil): List[String] = {
//                implicit val base_panel_local: String = base_path + company + output_local
//                implicit val titleSeq: List[String] = "ID" :: "Hosp_name" :: "Date" :: "Prod_Name" :: "Prod_CNAME" ::
//                                "HOSP_ID" :: "Strength" :: "DOI" :: "DOIE" :: "Units" :: "Sales" :: Nil
//
//                val page = pageMemory(source._1)
//                var file_lst = file_lst_arg
//
//                (0 until page.pageCount.toInt) foreach { i =>
//                    lazy val temp = page.pageData(i).map { line =>
//                        val data = source._2.zip(line.split(spl).toList).toMap
//                        if (data("YM") == ym && hos0_hosp_id.contains(data("HOSPITAL_CODE")))
//                            data ++ Map("min1" -> getMin1Fun(data))
//                        else
//                            Map[String, String]()
//                    }.filter(_ != Map())
//
//                    innerJoin(m1Arg, temp, "min1", "min1")
//                            .map(mergeMC(_, market, hosp_tab))
//                            .filter(_("Sales") != "")
//                            .foreach { x =>
//                                file_lst = file_lst :+ phHandleCsvImpl().sortInsert(x, file_lst, distinct_source, mergeSameLine)
//                                file_lst = file_lst.distinct
//                            }
//                }
//                page.ps.fs.closeStorage
//                file_lst
//            }
//
//            val panel_local = filter_source(g0, m1_g, filter_source(c0, m1_c))
//            market -> panel_local
//        }.toMap
//    }
//
