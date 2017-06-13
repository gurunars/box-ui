package com.gurunars.databinding

import android.view.View

fun<Type> View.bindableField(value: Type,
                             equal: (one: Type, two: Type) -> Boolean = ::equal,
                             preset: (one: Type) -> Type = { item -> item }): BindableField<Type> {
    val field = BindableField(value, equal, preset)
    this.addOnAttachStateChangeListener(object: View.OnAttachStateChangeListener {
        override fun onViewAttachedToWindow(v: View) {

        }

        override fun onViewDetachedFromWindow(v: View) {
            field.disposeAll()
        }
    })
    return field
}


fun<Type> DisposableRegistryService.bindableField(value: Type,
                                                  equal: (one: Type, two: Type) -> Boolean = ::equal,
                                                  preset: (one: Type) -> Type = { item -> item }): BindableField<Type> {
    val field = BindableField(value, equal, preset)
    this.add(field)
    return field
}
