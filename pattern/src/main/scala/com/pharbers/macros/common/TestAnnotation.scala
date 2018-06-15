package com.pharbers.macros.common

import scala.annotation.{StaticAnnotation, compileTimeOnly}
import scala.reflect.macros.whitebox
import scala.language.experimental.macros

import scala.reflect.runtime.{universe => ru}

@compileTimeOnly("调试的Annotation，为了实现从定义的类变成表达书树")
class TestAnnotation extends StaticAnnotation {
    def macroTransform(annottees : Any*) : Any = macro TestAnnotationMacro.impl
}

object TestAnnotationMacro {
    def impl(c: whitebox.Context)(annottees: c.Expr[Any]*): c.Expr[Any] = {
        import c.universe._
        val inputs = annottees.map(_.tree).toList

        val (s, annottee, expandees) = inputs match {
            case (param: ValDef) :: (rest@(_ :: _)) => ("val", param, rest)
            case (param: TypeDef) :: (rest@(_ :: _)) => ("type", param, rest)
            case _ => ("", EmptyTree, inputs)
        }
        println((s, annottee, expandees))
        val outputs = expandees
        println(showRaw(expandees.head))
        c.Expr[Any](Block(outputs, Literal(Constant(()))))
    }
}
