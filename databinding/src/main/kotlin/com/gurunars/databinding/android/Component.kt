package com.gurunars.databinding.android

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout

interface Component {
    fun Context.render(): View
}

fun Component.add(parent: ViewGroup) = parent.addView(parent.context.render())

fun Component.setAsOne(parent: FrameLayout) = parent.context.render().setAsOne(parent)

fun Component.setAsOne(parent: Activity) = parent.render().setAsOne(parent)