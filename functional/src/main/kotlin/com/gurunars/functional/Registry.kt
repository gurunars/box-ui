package com.gurunars.functional

import com.gurunars.functional.text.Text
import com.gurunars.functional.text.TextComponent

object Registry {

    class UnknownComponent(component: Any): Throwable(component.javaClass.canonicalName)

    fun<T> getElement(props: T): Component<T> =
        when(props) {
            is Text -> TextComponent() as Component<T>
            /*
            is Element -> LinearSlotComponent(
                getElement(props.child)
            )
            is LinearContainer -> LinearContainerComponent()
            */
            else -> throw UnknownComponent(props as Any)
        }

}