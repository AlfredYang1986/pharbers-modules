package com.pharbers.macros.common.resulting

import com.pharbers.jsonapi.JsonapiRootObjectFormat
import com.pharbers.jsonapi.model.{Attributes, RootObject}

import scala.reflect.macros.whitebox.Context
import scala.language.experimental.macros

trait Resultable[T] extends JsonapiRootObjectFormat[T] {
    override def toJsonapi(a : T) : RootObject
    override def fromJsonapi(obj : RootObject) : T
}

object Resultable {
    implicit def materializeResultable[T] : Resultable[T] = macro impl[T]

    def impl[T](c: Context)(ttag: c.WeakTypeTag[T]) : c.Expr[Resultable[T]] = {
        import c.universe._
        import c.universe.Flag._

        val fresh_class_name = TypeName(c.freshName("eval$"))

        val import_def =
            Import(Select(Select(Select(Ident(TermName("com")), TermName("pharbers")), TermName("model")), TermName("detail")),
                List(ImportSelector(termNames.WILDCARD, -1, null, -1))) ::
            Import(Select(Select(Select(Select(Ident(TermName("com")), TermName("pharbers")), TermName("jsonapi")), TermName("model")), TermName("JsonApiObject")),
                List(ImportSelector(TermName("NumberValue"), -1, TermName("NumberValue"), -1),
                    ImportSelector(TermName("JsObjectValue"), -1, TermName("JsObjectValue"), -1),
                    ImportSelector(TermName("NullValue"), -1, TermName("NullValue"), -1),
                    ImportSelector(TermName("StringValue"), -1, TermName("StringValue"), -1))) ::
            Import(Select(Select(Select(Ident(TermName("com")), TermName("pharbers")), TermName("jsonapi")), TermName("model")),
                List(ImportSelector(TermName("Attribute"), -1, TermName("Attribute"), -1),
                    ImportSelector(TermName("Attributes"), -1, TermName("Attributes"), -1),
                    ImportSelector(TermName("Links"), -1, TermName("Links"), -1),
                    ImportSelector(TermName("RootObject"), -1, TermName("RootObject"), -1))) ::
            Import(Select(Select(Select(Select(Ident(TermName("com")), TermName("pharbers")), TermName("jsonapi")), TermName("model")), TermName("RootObject")),
                List(ImportSelector(TermName("ResourceObject"), -1, TermName("ResourceObject"), -1),
                    ImportSelector(TermName("ResourceObjects"), -1, TermName("ResourceObjects"), -1))) ::
            Import(Select(Ident(TermName("scala")), TermName("reflect")),
                List(ImportSelector(TermName("ClassTag"), -1, TermName("ClassTag"), -1))) ::
            Import(Select(Select(Select(Ident(TermName("scala")), TermName("reflect")), TermName("runtime")), TermName("universe")),
                List(ImportSelector(termNames.WILDCARD, -1, null, -1))) ::
            Import(Select(Select(Ident(TermName("scala")), TermName("reflect")), TermName("runtime")),
                  List(ImportSelector(TermName("universe"), -1, TermName("ru"), -1))) :: Nil

        val toJsonapiFuncDef =
            DefDef(Modifiers(OVERRIDE), TermName("toJsonapi"), List(),
                List(List(ValDef(Modifiers(PARAM), TermName("p"), Ident(TypeName("userdetailresult")), EmptyTree))),
                TypeTree(),
                Block(List(
                    ValDef(Modifiers(), TermName("mirror"), TypeTree(), Apply(Select(Ident(TermName("ru")), TermName("runtimeMirror")),
                        List(Select(Ident(TermName("getClass")), TermName("getClassLoader"))))),
                    ValDef(Modifiers(), TermName("inst_mirror"), TypeTree(), Apply(Select(Ident(TermName("mirror")), TermName("reflect")),
                        List(Ident(TermName("p"))))),
                    ValDef(Modifiers(), TermName("class_symbol"), TypeTree(), Select(Ident(TermName("inst_mirror")), TermName("symbol"))),
                    ValDef(Modifiers(), TermName("class_field"), TypeTree(),
                        Select(Apply(Select(Select(Select(Ident(TermName("class_symbol")),
                            TermName("typeSignature")), TermName("members")), TermName("filter")),
                            List(Function(List(ValDef(Modifiers(PARAM), TermName("p"), TypeTree(), EmptyTree)),
                                Apply(Select(Select(Ident(TermName("p")), TermName("isTerm")), TermName("$amp$amp")),
                                    List(Select(Select(Ident(TermName("p")), TermName("isMethod")), TermName("unary_$bang"))))))),
                            TermName("toList"))),
                    ValDef(Modifiers(), TermName("attrs"), TypeTree(), TypeApply(Select(Apply(Select(Apply(Select(Ident(TermName("class_field")), TermName("map")),
                        List(Function(List(ValDef(Modifiers(PARAM), TermName("f"), TypeTree(), EmptyTree)),
                            Block(List(
                                ValDef(Modifiers(), TermName("attr_mirror"), TypeTree(),
                                    Apply(Select(Ident(TermName("inst_mirror")), TermName("reflectField")),
                                        List(Select(Ident(TermName("f")), TermName("asTerm"))))),
                                ValDef(Modifiers(), TermName("attr_val"), TypeTree(), Select(Ident(TermName("attr_mirror")), TermName("get")))),
                                Apply(Ident(TermName("Attribute")),
                                    List(Select(Select(Ident(TermName("f")), TermName("name")), TermName("toString")),
                                        If(Apply(Select(Select(Ident(TermName("f")), TermName("info")), TermName("$eq$colon$eq")),
                                            List(TypeApply(Ident(TermName("typeOf")), List(Ident(TypeName("String")))))),
                                            Apply(Ident(TermName("StringValue")), List(Select(Ident(TermName("attr_val")), TermName("toString")))),
                                            If(Apply(Select(Select(Ident(TermName("f")), TermName("info")), TermName("$less$colon$less")),
                                                List(TypeApply(Ident(TermName("typeOf")), List(Ident(TypeName("Number")))))),
                                                Apply(Ident(TermName("NumberValue")), List(Apply(Ident(TermName("BigDecimal")),
                                                    List(Select(TypeApply(Select(Ident(TermName("attr_val")), TermName("asInstanceOf")),
                                                        List(Ident(TypeName("Number")))), TermName("doubleValue")))))),
                                                Ident(TermName("NullValue")))))))))), TermName("filterNot")),
                        List(Function(List(ValDef(Modifiers(PARAM), TermName("it"), TypeTree(), EmptyTree)),
                            Apply(Select(Ident(TermName("NullValue")), TermName("$eq$eq")), List(Select(Ident(TermName("it")), TermName("value"))))))),
                        TermName("asInstanceOf")), List(Ident(TypeName("Attributes")))))),
                    Apply(Ident(TermName("RootObject")), List(AssignOrNamedArg(Ident(TermName("data")),
                        Apply(Ident(TermName("Some")), List(Apply(Ident(TermName("ResourceObject")),
                            List(AssignOrNamedArg(Ident(TermName("type")), Literal(Constant("userdetailresult"))),
                                AssignOrNamedArg(Ident(TermName("id")), Apply(Ident(TermName("Some")),
                                    List(Select(Select(Ident(TermName("p")), TermName("id")), TermName("toString"))))),
                                AssignOrNamedArg(Ident(TermName("attributes")), Apply(Ident(TermName("Some")),
                                    List(Ident(TermName("attrs"))))),
                                AssignOrNamedArg(Ident(TermName("links")),
                                    Apply(Ident(TermName("Some")), List(Apply(Ident(TermName("List")),
                                        List(Apply(Select(Ident(TermName("Links")), TermName("Self")),
                                            List(Literal(Constant("http://test.link/person/42")), Ident(TermName("None"))))))))))))))))
            ))

//            DefDef(Modifiers(OVERRIDE), TermName("toJsonapi"), List(),
//                List(List(ValDef(Modifiers(PARAM), TermName("udr"), Ident(TypeName("userdetailresult")), EmptyTree))),
//                TypeTree(),
//                Apply(Ident(TermName("RootObject")),
//                    List(AssignOrNamedArg(Ident(TermName("data")), Apply(Ident(TermName("Some")),
//                        List(Apply(Ident(TermName("ResourceObject")),
//                            List(AssignOrNamedArg(Ident(TermName("type")), Literal(Constant("userdetailresult"))),
//                                AssignOrNamedArg(Ident(TermName("id")), Apply(Ident(TermName("Some")),
//                                    List(Select(Select(Ident(TermName("udr")), TermName("id")), TermName("toString"))))),
//                                AssignOrNamedArg(Ident(TermName("attributes")), Apply(Ident(TermName("Some")),
//                                    List(Apply(Ident(TermName("List")),
//                                        List(Apply(Ident(TermName("Attribute")), List(Literal(Constant("major")),
//                                            Apply(Ident(TermName("NumberValue")), List(Apply(Ident(TermName("BigDecimal")),
//                                                List(Select(Ident(TermName("udr")), TermName("major")))))))),
//                                            Apply(Ident(TermName("Attribute")), List(Literal(Constant("minor")),
//                                                Apply(Ident(TermName("NumberValue")), List(Apply(Ident(TermName("BigDecimal")),
//                                                    List(Select(Ident(TermName("udr")), TermName("minor")))))))),
//                                            Apply(Ident(TermName("Attribute")), List(Literal(Constant("user")),
//                                                Apply(Ident(TermName("JsObjectValue")), List(Apply(Ident(TermName("asJsonApi")),
//                                                    List(Select(Select(Ident(TermName("udr")), TermName("user")), TermName("get")))))))),
//                                            Apply(Ident(TermName("Attribute")), List(Literal(Constant("company")),
//                                                Apply(Ident(TermName("JsObjectValue")), List(Apply(Ident(TermName("asJsonApi")),
//                                                    List(Select(Select(Ident(TermName("udr")), TermName("company")), TermName("get"))))))))))))),
//                                AssignOrNamedArg(Ident(TermName("links")), Apply(Ident(TermName("Some")), List(Apply(Ident(TermName("List")),
//                                    List(Apply(Select(Ident(TermName("Links")), TermName("Self")),
//                                        List(Literal(Constant("http://test.link/person/42")),
//                                            Ident(TermName("None")))))))))))))))))

        val tmp = ClassDef(Modifiers(), fresh_class_name, List(), Template(List(AppliedTypeTree(Ident(TypeName("Resultable")),
            List(Ident(TypeName("userdetailresult"))))), noSelfType,
            List(
                DefDef(Modifiers(), termNames.CONSTRUCTOR, List(), List(List()), TypeTree(),
                    Block(List(pendingSuperCall), Literal(Constant(())))),
                toJsonapiFuncDef,
                DefDef(Modifiers(OVERRIDE), TermName("fromJsonapi"), List(), List(List(ValDef(Modifiers(PARAM), TermName("rootObject"),
                    Ident(TypeName("RootObject")), EmptyTree))), Ident(TypeName("userdetailresult")), Ident(TermName("$qmark$qmark$qmark"))))))

//        val ttt = c.Expr[Resultable[T]](Block(import_def ::: (tmp :: Nil), Apply(Select(New(Ident(fresh_class_name)), termNames.CONSTRUCTOR), List())))
//        println(ttt)
//        ttt
        c.Expr[Resultable[T]](Block(import_def ::: (tmp :: Nil), Apply(Select(New(Ident(fresh_class_name)), termNames.CONSTRUCTOR), List())))
    }
}

