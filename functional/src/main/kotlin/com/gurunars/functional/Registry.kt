package com.gurunars.functional

import com.gurunars.functional.text.Text
import com.gurunars.functional.text.TextBinder

object Registry {

    class UnknownComponent(component: Any): Throwable(component.javaClass.canonicalName)

    fun<T> getParams(props: T): ParamBinder =
        when(props) {
            is Linear
            else -> throw UnknownComponent(props as Any)
        }

    fun<T> getElement(props: T): ElementBinder =
        when(props) {
            is View -> ViewBinder(
                getElement(props.child),
                getParams(props.layoutParams)
            )
            is Text -> TextBinder()
            is LinearContainer -> LinearContainerBinder()
            else -> throw UnknownComponent(props as Any)
        }

}