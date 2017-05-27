package com.gurunars.databinding.android

import android.view.View
import com.gurunars.databinding.BindableField

fun<Type> View.bindableField(
        value: Type, preset: (one: Type) -> Type = { item -> item }): BindableField<Type> {
    val field = BindableField(value, preset)
    this.addOnAttachStateChangeListener(object: View.OnAttachStateChangeListener {
        override fun onViewAttachedToWindow(v: View) {

        }

        override fun onViewDetachedFromWindow(v: View) {
            field.unbindAll()
        }
    })
    return field
}