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
        import c.universe.Flag._

        val t_name = ttag.tpe match { case TypeRef(_, str, _) => str }
        val weak_type_name = t_name.asClass.name.toString
        println(weak_type_name)
        val fresh_class_name = TypeName(c.freshName("eval$"))

        val import_def =
            Import(Select(Select(Select(Ident(TermName("com")), TermName("pharbers")), TermName("model")), TermName("detail")),
                List(ImportSelector(termNames.WILDCARD, 609, null, -1))) ::
            Import(Select(Select(Select(Ident(TermName("com")), TermName("pharbers")), TermName("jsonapi")), TermName("model")),
                List(ImportSelector(TermName("Attribute"), 650, TermName("Attribute"), 650), ImportSelector(TermName("Attributes"), 661, TermName("Attributes"), 661))) ::
            Import(Select(Select(Select(Select(Ident(TermName("com")), TermName("pharbers")), TermName("jsonapi")), TermName("model")), TermName("RootObject")),
                List(ImportSelector(TermName("ResourceObject"), 722, TermName("ResourceObject"), 722))) ::
            Import(Select(Select(Select(Select(Ident(TermName("com")), TermName("pharbers")), TermName("jsonapi")), TermName("model")), TermName("JsonApiObject")),
                List(ImportSelector(TermName("StringValue"), 789, TermName("StringValue"), 789))) ::
            Import(Select(Select(Select(Select(Ident(TermName("com")), TermName("pharbers")), TermName("jsonapi")), TermName("model")), TermName("JsonApiObject")),
                List(ImportSelector(TermName("NumberValue"), 853, TermName("NumberValue"), 853))) ::
            Import(Select(Select(Select(Select(Ident(TermName("com")), TermName("pharbers")), TermName("jsonapi")), TermName("model")), TermName("JsonApiObject")),
                List(ImportSelector(TermName("NullValue"), 917, TermName("NullValue"), 917))) ::
            Import(Select(Ident(TermName("scala")), TermName("reflect")),
                List(ImportSelector(TermName("ClassTag"), 952, TermName("ClassTag"), 952))) ::
            Import(Select(Select(Select(Ident(TermName("scala")), TermName("reflect")), TermName("runtime")), TermName("universe")),
                List(ImportSelector(termNames.WILDCARD, 1004, null, -1))) ::
            Import(Select(Select(Ident(TermName("scala")), TermName("reflect")), TermName("runtime")),
                List(ImportSelector(TermName("universe"), 1040, TermName("ru"), 1052))) :: Nil

        val tmp =
            ClassDef(Modifiers(), fresh_class_name, List(),
                Template(List(AppliedTypeTree(Ident(TypeName("Expandable")),
                    List(Select(Select(Select(Select(Ident(TermName("com")), TermName("pharbers")),
                        TermName("model")), TermName("detail")), TypeName(weak_type_name))))), noSelfType,
                    List(DefDef(Modifiers(), termNames.CONSTRUCTOR, List(), List(List()), TypeTree(),
                        Block(List(pendingSuperCall), Literal(Constant(())))),
                        DefDef(Modifiers(OVERRIDE), TermName("toJsonapi"), List(), List(List(ValDef(Modifiers(PARAM), TermName("p"), Ident(TypeName(weak_type_name)),
                            EmptyTree))), TypeTree(), Block(List(ValDef(Modifiers(), TermName("mirror"), TypeTree(), Apply(Select(Ident(TermName("ru")),
                            TermName("runtimeMirror")), List(Select(Ident(TermName("getClass")), TermName("getClassLoader"))))), ValDef(Modifiers(),
                            TermName("inst_mirror"), TypeTree(), Apply(Select(Ident(TermName("mirror")), TermName("reflect")), List(Ident(TermName("p"))))),
                            ValDef(Modifiers(), TermName("class_symbol"), TypeTree(), Select(Ident(TermName("inst_mirror")), TermName("symbol"))),
                            ValDef(Modifiers(), TermName("class_field"), TypeTree(), Select(Apply(Select(Select(Select(Ident(TermName("class_symbol")),
                                TermName("typeSignature")), TermName("members")), TermName("filter")), List(Function(List(ValDef(Modifiers(PARAM),
                                TermName("p"), TypeTree(), EmptyTree)), Apply(Select(Select(Ident(TermName("p")), TermName("isTerm")), TermName("$amp$amp")),
                                List(Select(Select(Ident(TermName("p")), TermName("isMethod")), TermName("unary_$bang"))))))), TermName("toList")))),
                            TypeApply(Select(Apply(Select(Apply(Select(Ident(TermName("class_field")), TermName("map")), List(Function(
                                List(ValDef(Modifiers(PARAM), TermName("f"), TypeTree(), EmptyTree)), Block(List(ValDef(Modifiers(),
                                    TermName("attr_mirror"), TypeTree(), Apply(Select(Ident(TermName("inst_mirror")), TermName("reflectField")),
                                        List(Select(Ident(TermName("f")), TermName("asTerm"))))), ValDef(Modifiers(), TermName("attr_val"), TypeTree(),
                                    Select(Ident(TermName("attr_mirror")), TermName("get")))), Apply(Ident(TermName("Attribute")),
                                    List(Select(Select(Ident(TermName("f")), TermName("name")), TermName("toString")),
                                        If(Apply(Select(Select(Ident(TermName("f")), TermName("info")), TermName("$eq$colon$eq")),
                                            List(TypeApply(Ident(TermName("typeOf")), List(Ident(TypeName("String")))))),
                                            Apply(Ident(TermName("StringValue")), List(Select(Ident(TermName("attr_val")), TermName("toString")))),
                                            If(Apply(Select(Select(Ident(TermName("f")), TermName("info")), TermName("$less$colon$less")),
                                                List(TypeApply(Ident(TermName("typeOf")), List(Ident(TypeName("Number")))))),
                                                Apply(Ident(TermName("NumberValue")), List(Apply(Ident(TermName("BigDecimal")),
                                                    List(Select(TypeApply(Select(Ident(TermName("attr_val")), TermName("asInstanceOf")),
                                                        List(Ident(TypeName("Number")))), TermName("doubleValue")))))), Ident(TermName("NullValue")))))))))),
                                TermName("filterNot")), List(Function(List(ValDef(Modifiers(PARAM), TermName("it"), TypeTree(), EmptyTree)),
                                Apply(Select(Ident(TermName("NullValue")), TermName("$eq$eq")), List(Select(Ident(TermName("it")), TermName("value"))))))),
                                TermName("asInstanceOf")), List(Ident(TypeName("Attributes")))))), DefDef(Modifiers(OVERRIDE), TermName("fromJsonapi"),
                            List(), List(List(ValDef(Modifiers(PARAM), TermName("obj"), Ident(TypeName("ResourceObject")), EmptyTree))),
                            Ident(TypeName(weak_type_name)), Ident(TermName("$qmark$qmark$qmark"))))))

       val ttt = c.Expr[Expandable[T]](Block(import_def ::: (tmp :: Nil), Apply(Select(New(Ident(fresh_class_name)), termNames.CONSTRUCTOR), List())))
       println(ttt)
       ttt
    }
}
