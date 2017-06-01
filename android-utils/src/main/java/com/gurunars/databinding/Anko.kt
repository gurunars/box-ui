package com.gurunars.databinding

import android.view.View

fun<Type> View.field(value: Type): BindableField<Type> {
    val field = BindableField(value)
    this.addOnAttachStateChangeListener(object: View.OnAttachStateChangeListener {
        override fun onViewAttachedToWindow(v: View) {

        }

        override fun onViewDetachedFromWindow(v: View) {

        }
    })
    return field
}