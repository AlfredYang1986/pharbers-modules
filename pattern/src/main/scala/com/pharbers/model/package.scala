package com.pharbers

import com.pharbers.jsonapi.model.{Attributes, RootObject}
import com.pharbers.macros.common.expending.Expandable
import com.pharbers.macros.common.resulting.Resultable

package object model {
    def asJsonApi[T](x : T)(implicit exTag : Expandable[T]) : Attributes = exTag.toJsonapi(x)
    def asJsonApiResult[T](x : T)(implicit exTag : Resultable[T]) : RootObject = exTag.toJsonapi(x)
}
