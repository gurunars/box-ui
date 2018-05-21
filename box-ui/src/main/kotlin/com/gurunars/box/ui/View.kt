package com.gurunars.box.ui

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import com.gurunars.box.core.ObservableValue
import com.gurunars.box.core.IReadOnlyObservableValue

var View.isVisible by PlainConsumerField<View, Boolean> {
    visibility = if (it) View.VISIBLE else View.GONE
}

fun View.onLongClick(callback: () -> Unit) = setOnLongClickListener {
    callback()
    true
}

fun View.onClick(callback: () -> Unit) = setOnClickListener {
    callback()
}


var View.padding by PlainConsumerField<View, Bounds> {
    setPadding(it.left, it.top, it.right, it.bottom)
}

var View.visibility_ by ConsumerField<View, Int> { visibility = it }
var View.isActivated_ by ConsumerField<View, Boolean> { isActivated = it }
var View.isSelected_ by ConsumerField<View, Boolean> { isSelected = it }
var View.translationZ_ by ConsumerField<View, Float> { translationZ = it }
var View.translationY_ by ConsumerField<View, Float> { translationY = it }
var View.translationX_ by ConsumerField<View, Float> { translationX = it }
var View.elevation_ by ConsumerField<View, Float> { elevation = it }
var View.alpha_ by ConsumerField<View, Float> { alpha = it }
var View.pivotY__ by ConsumerField<View, Float> { pivotY = it }
var View.pivotX_ by ConsumerField<View, Float> { pivotX = it }
var View.scaleY_ by ConsumerField<View, Float> { scaleY = it }
var View.scaleX_ by ConsumerField<View, Float> { scaleX = it }
var View.rotationX_ by ConsumerField<View, Float> { rotationX = it }
var View.rotationY_ by ConsumerField<View, Float> { rotationY = it }
var View.rotation_ by ConsumerField<View, Float> { rotation = it }
var View.isClickable_ by ConsumerField<View, Boolean> { isClickable = it }
var View.isLongClickable_ by ConsumerField<View, Boolean> { isLongClickable = it }
var View.isFocusable_ by ConsumerField<View, Boolean> { isFocusable = it }
var View.isEnabled_ by ConsumerField<View, Boolean> { isEnabled = it }
var View.background_ by ConsumerField<View, Drawable> { background = it }
var View.isVisible_ by ConsumerField<View, Boolean> { isVisible = it }
var View.padding_ by ConsumerField<View, Bounds> { padding = it  }

fun Context.view(init: View.() -> Unit = {}) = View(this).apply {
    init()
}

val View.attachedToWindow: IReadOnlyObservableValue<Boolean>
    get() = ObservableValue(false).apply {
        addOnAttachStateChangeListener(object: View.OnAttachStateChangeListener {
            override fun onViewDetachedFromWindow(v: View?) { set(false) }
            override fun onViewAttachedToWindow(v: View?) { set(true) }
        })
    }