package com.gurunars.functional

import android.widget.LinearLayout as AndroidLinearLayout

object Registry {

    class UnknownComponent(component: Any): Throwable(component.javaClass.canonicalName)

    fun<T> getParams(props: T): ParamBinder =
        when(props) {
            is LinearLayoutParams -> LinearLayoutParamsBinder()
            is RelativeLayoutParams -> RelativeLayoutParamsBinder()
            else -> throw UnknownComponent(props as Any)
        }

    fun<T> getElement(props: T): ElementBinder =
        when(props) {
            is View -> ViewBinder(
                getElement(props.child),
                getParams(props.layoutParams)
            )
            is Text -> TextBinder()
            is LinearLayout -> LinearContainerBinder()
            is RelativeLayout -> RelativeContainerBinder()
            else -> throw UnknownComponent(props as Any)
        }

}