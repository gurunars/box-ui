package com.gurunars.databinding

import android.view.View

fun<Type> View.bindableField(value: Type): BindableField<Type> {
    val field = BindableField(value)
    this.addOnAttachStateChangeListener(object: View.OnAttachStateChangeListener {
        override fun onViewAttachedToWindow(v: View) {

        }

        override fun onViewDetachedFromWindow(v: View) {
            field.disposeAll()
        }
    })
    return field
}

fun<Type> DisposableRegistry.bindableField(value: Type): BindableField<Type> {
    val field = BindableField(value)
    this.add(field)
    return field
}
