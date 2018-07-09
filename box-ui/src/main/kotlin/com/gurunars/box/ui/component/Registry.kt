package com.gurunars.box.ui.component

object Registry {

    class UnknownComponent(component: Any): Throwable(component.javaClass.canonicalName)

    fun<T> getElement(props: T): Component =
        when(props) {
            is Label -> TextComponent()
            is LinearSlot -> LinearSlotComponent(
                getElement(props.child)
            )
            is LinearContainer -> LinearContainerComponent()
            else -> throw UnknownComponent(props as Any)
        }

}