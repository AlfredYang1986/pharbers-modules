package com.pharbers.pharbersmacro

import com.mongodb.DBObject
import play.api.libs.json.JsValue
import scala.language.experimental.macros
import scala.reflect.macros.whitebox.Context

object CURDMacro {
    def pushMacro(d2m : JsValue => DBObject,
                  ssr : DBObject => Map[String, JsValue],
                  data : JsValue,
                  db : String,
                  outter : String) : (Option[Map[String, JsValue]], Option[JsValue]) = macro pushMacrosImpls.macroImpl
    class pushMacrosImpls(val c: Context) {
        import c.universe._
        def macroImpl(d2m : c.Tree, ssr : c.Tree, data : c.Tree, db : c.Tree, outter : c.Tree): c.Tree = {
            println("push macro compiling ...")
            q"""
                {
                    implicit val d2m = $d2m
                    implicit val ssr = $ssr
                    processor (value => returnValue(creation(value)($db), $outter))($data)
                }
            """
        }
    }


    def popMacro(qc : JsValue => DBObject,
                 popr : DBObject => Map[String, JsValue],
                 data : JsValue,
                 db : String) : (Option[Map[String, JsValue]], Option[JsValue]) = macro popMacrosImpls.macroImpl
    class popMacrosImpls(val c: Context) {
        import c.universe._
        def macroImpl(qc : c.Tree, popr : c.Tree, data : c.Tree, db : c.Tree): c.Tree = {
            println("pop macro compiling ...")
            q"""
                {
                    implicit val qc = $qc
                    implicit val popr = $popr
                    processor (value => returnValue(remove(value)($db), ""))($data)
                }
            """
        }
    }


    def queryMacro(qc : JsValue => DBObject,
                   dr : DBObject => Map[String, JsValue],
                   data : JsValue,
                   db : String,
                   outter : String) : (Option[Map[String, JsValue]], Option[JsValue]) = macro queryMacrosImpls.macroImpl
    class queryMacrosImpls(val c: Context) {
        import c.universe._
        def macroImpl(qc : c.Tree, dr : c.Tree, data : c.Tree, db : c.Tree, outter : c.Tree): c.Tree = {
            println("query macro compiling ...")
            q"""
                {
                    implicit val qc = $qc
                    implicit val dr = $dr
                    processor (value => returnValue(query(value)($db), $outter))($data)
                }
            """
        }
    }


    def queryMultiMacro(qcm : JsValue => DBObject,
                        sr : DBObject => Map[String, JsValue],
                        data : JsValue,
                        db : String,
                        outter : String) : (Option[Map[String, JsValue]], Option[JsValue]) = macro queryMultiMacrosImpls.macroImpl
    class queryMultiMacrosImpls(val c: Context) {
        import c.universe._
        def macroImpl(qcm : c.Tree, sr : c.Tree, data : c.Tree, db : c.Tree, outter : c.Tree): c.Tree = {
            println("query multi macro compiling ...")
            q"""
                {
                    implicit val qcm = $qcm
                    implicit val sr = $sr
                    processor (value => returnValue(queryMulti(value)($db), $outter))($data)
                }
            """
        }
    }


    def updateMacro(qcm: JsValue => DBObject,
                    up: (DBObject, JsValue) => DBObject,
                    sr: DBObject => Map[String, JsValue],
                    data: JsValue,
                    db: String,
                    outter: String): (Option[Map[String, JsValue]], Option[JsValue]) = macro updateMacrosImpls.macroImpl
    class updateMacrosImpls(val c: Context) {
        import c.universe._
        def macroImpl(qcm : c.Tree, up : c.Tree, sr : c.Tree, data : c.Tree, db : c.Tree, outter : c.Tree): c.Tree = {
            println("update macro compiling ...")
            q"""
                {
                    implicit val qcm = $qcm
                    implicit val up = $up
                    implicit val sr = $sr
                    processor (value => returnValue(update(value)($db), $outter))($data)
                }
            """
        }
    }
}
