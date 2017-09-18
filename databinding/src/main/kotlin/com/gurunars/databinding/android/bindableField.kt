package com.gurunars.databinding.android

import android.view.View
import com.gurunars.databinding.BindableField

/**
 * Attach field's lifecycle to the view.
 */
fun View.possess(vararg fields: BindableField<*>) = fields.forEach {
    addOnAttachStateChangeListener(object: View.OnAttachStateChangeListener {
        override fun onViewAttachedToWindow(v: View) {
            it.resume()
        }

        override fun onViewDetachedFromWindow(v: View) {
            it.pause()
        }
    })
}

/**
 * Obtain a bindable field for a given view and register it for unbinding via
 * OnAttachStateChangeListener.onViewDetachedFromWindow.
 */
fun<Type> View.bindableField(
        value: Type, preset: (one: Type) -> Type = { item -> item }) =
BindableField(value, preset).apply {
    possess(this)
}
