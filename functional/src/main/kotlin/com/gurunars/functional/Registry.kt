package com.gurunars.functional

import android.widget.LinearLayout as AndroidLinearLayout

object Registry {

    class UnknownComponent(component: Any): Throwable(component.javaClass.canonicalName)

    fun<T> getParamBinder(props: T): ParamBinder =
        when(props) {
            is LinearLayoutParams -> LinearLayoutParamsBinder()
            is RelativeLayoutParams -> RelativeLayoutParamsBinder()
            else -> throw UnknownComponent(props as Any)
        }

    fun<T> getElementBinder(props: T): ElementBinder =
        when(props) {
            is View -> ViewBinder(
                getElementBinder(props.child),
                getParamBinder(props.layoutParams)
            )
            is Text -> TextBinder()
            is LinearLayout -> LinearContainerBinder()
            is RelativeLayout -> RelativeContainerBinder()
            is TextInput -> TextInputBinder()
            else -> StringBinder()
        }

}