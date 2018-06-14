package com.pharbers.macros.common.expending

import com.pharbers.jsonapi.JsonapiResourceObjectFormat
import com.pharbers.jsonapi.model.{Attributes, RootObject}

import scala.reflect.macros.whitebox.Context
import scala.language.experimental.macros

trait Expandable[T] extends JsonapiResourceObjectFormat[T] {
    override def toJsonapi(a : T) : Attributes
    override def fromJsonapi(obj : RootObject.ResourceObject) : T
}

object Expandable {
    implicit def materializeExpandable[T] : Expandable[T] = macro impl[T]

    def impl[T](c: Context)(ttag: c.WeakTypeTag[T]) : c.Expr[Expandable[T]] = {
        import c.universe._

        ttag.tpe.baseClasses.foreach(println(_))
        val t_name = ttag.tpe match { case TypeRef(_, str, _) => str }
        println(s"type of ttag has type arguments $t_name")

        val tmp =
        ClassDef(Modifiers(), TypeName("userJsonApiOpt"), List(),
            Template(List(AppliedTypeTree(Ident(TypeName("Expandable")), List(Ident(TypeName("user"))))), noSelfType,
                List(DefDef(Modifiers(), termNames.CONSTRUCTOR, List(), List(List()), TypeTree(),
                    Block(List(pendingSuperCall), Literal(Constant(())))),
                    Import(Select(Select(Select(Select(Ident(TermName("com")),
                        TermName("pharbers")), TermName("jsonapi")), TermName("model")), TermName("JsonApiObject")),
                        List(ImportSelector(TermName("StringValue"), 499, TermName("StringValue"), 499))),
                    Import(Select(Select(Select(Ident(TermName("com")), TermName("pharbers")), TermName("jsonapi")), TermName("model")),
                        List(ImportSelector(TermName("Attribute"), 550, TermName("Attribute"), 550),
                            ImportSelector(TermName("Attributes"), 561, TermName("Attributes"), 561))),
                    Import(Select(Select(Select(Select(Ident(TermName("com")), TermName("pharbers")), TermName("jsonapi")),
                        TermName("model")), TermName("RootObject")),
                        List(ImportSelector(TermName("ResourceObject"), 622, TermName("ResourceObject"), 622))),
                    Import(Select(Select(Ident(TermName("org")), TermName("bson")), TermName("types")),
                        List(ImportSelector(TermName("ObjectId"), 663, TermName("ObjectId"), 663))),
                    DefDef(Modifiers(Flag.OVERRIDE), TermName("toJsonapi"), List(),
                        List(List(ValDef(Modifiers(Flag.PARAM), TermName("p"), Ident(TypeName("user")), EmptyTree))), TypeTree(),
                        TypeApply(Select(Block(List(ValDef(Modifiers(Flag.SYNTHETIC | Flag.ARTIFACT), TermName("x$2"), TypeTree(),
                            Apply(Ident(TermName("Attribute")), List(Literal(Constant("name")), Apply(Ident(TermName("StringValue")),
                                List(Select(Ident(TermName("p")), TermName("name")))))))),
                            Apply(Select(Block(List(ValDef(Modifiers(Flag.SYNTHETIC | Flag.ARTIFACT), TermName("x$1"), TypeTree(),
                                Apply(Ident(TermName("Attribute")), List(Literal(Constant("photo")),
                                    Apply(Ident(TermName("StringValue")), List(Select(Ident(TermName("p")), TermName("photo")))))))),
                                Apply(Select(Ident(TermName("Nil")), TermName("$colon$colon")), List(Ident(TermName("x$1"))))),
                                TermName("$colon$colon")), List(Ident(TermName("x$2"))))), TermName("asInstanceOf")),
                            List(Ident(TypeName("Attributes"))))),
                    DefDef(Modifiers(Flag.OVERRIDE), TermName("fromJsonapi"), List(), List(List(
                        ValDef(Modifiers(Flag.PARAM), TermName("obj"), Ident(TypeName("ResourceObject")), EmptyTree))),
                        Ident(TypeName("user")), Apply(Ident(TermName("user")), List(Select(Select(Ident(TermName("ObjectId")),
                            TermName("get")), TermName("toString")), Literal(Constant("alfred yang")), Literal(Constant("photo")),
                            Ident(TermName("Nil"))))))))

       val ttt = c.Expr[Expandable[T]](Block(tmp :: Nil, Apply(Select(New(Ident(TypeName("userJsonApiOpt"))), termNames.CONSTRUCTOR), List())))
       println(ttt)
       ttt
    }
}
