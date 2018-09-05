package com.gurunars.functional

import com.gurunars.functional.text.Text
import com.gurunars.functional.text.TextBinder

object Registry {

    class UnknownComponent(component: Any): Throwable(component.javaClass.canonicalName)

    fun<T> getElement(props: T): ElementBinder =
        when(props) {
            is Text -> TextBinder()
            is LinearSlot -> LinearSlotBinder(
                getElement(props.child)
            )
            is LinearContainer -> LinearContainerBinder()
            else -> throw UnknownComponent(props as Any)
        }

}