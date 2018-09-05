package com.gurunars.functional

import android.view.ViewGroup
import com.gurunars.functional.text.Text
import com.gurunars.functional.text.TextBinder

object Registry {

    class UnknownComponent(component: Any): Throwable(component.javaClass.canonicalName)

    fun<T> getElement(props: T): ElementBinder =
        when(props) {
            is View -> ViewBinder(
                getElement(props.child),
                getElement(props.layoutParams) as Binder<LayoutParams, ViewGroup.LayoutParams>
            )
            is Text -> TextBinder()
            is LinearContainer -> LinearContainerBinder()
            else -> throw UnknownComponent(props as Any)
        }

}