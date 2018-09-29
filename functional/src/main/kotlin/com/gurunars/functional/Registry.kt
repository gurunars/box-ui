package com.gurunars.functional

import com.gurunars.functional.text.Text
import com.gurunars.functional.text.TextBinder
import android.widget.LinearLayout as AndroidLinearLayout

object Registry {

    class UnknownComponent(component: Any): Throwable(component.javaClass.canonicalName)

    fun<T> getParams(props: T): ParamBinder =
        when(props) {
            is LinearLayoutParams -> LinearLayoutParamsBinder()
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
            else -> throw UnknownComponent(props as Any)
        }

}