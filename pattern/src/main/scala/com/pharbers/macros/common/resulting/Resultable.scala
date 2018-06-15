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
        c.Expr[Resultable[T]](Block(Nil, Literal(Constant(()))))
    }
}

