@file:Suppress("NOTHING_TO_INLINE")

package com.gurunars.box.ui

import android.graphics.drawable.Drawable
import android.view.View
import com.gurunars.box.IRoBox

/** Binds to a boxed color int. */
inline fun View.backgroundColor(field: IRoBox<Int>) =
    field.onChange(listener = this::setBackgroundColor)

/** Binds to a boxed image resource int. */
inline fun View.backgroundRes(field: IRoBox<Int>) =
    field.onChange(listener = this::setBackgroundResource)

/** Binds to a boxed drawable. */
inline fun View.backgroundImage(field: IRoBox<Drawable>) =
    field.onChange(listener = this::setBackground)

/** Binds View.VISIBLE and View.GONE to boxed true/false boolean values respectively. */
inline fun View.isVisible(field: IRoBox<Boolean>) =
    field.onChange { status ->
        visibility = if (status) View.VISIBLE else View.GONE
    }