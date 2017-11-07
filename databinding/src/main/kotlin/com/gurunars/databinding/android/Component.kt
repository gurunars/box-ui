package com.gurunars.databinding.android

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout

interface Component {
    fun Context.render(): View
}

fun Component.add(parent: ViewGroup, init: View.() -> Unit = {}) =
    parent.context.render().add(parent, init)

fun Component.set(parent: ViewGroup, id: Int, init: View.() -> Unit = {}) =
    parent.context.render().set(parent, id, init)

fun Component.setAsOne(parent: FrameLayout, init: View.() -> Unit = {}) =
    parent.context.render().setAsOne(parent, init)

fun Component.setAsOne(parent: Activity, init: View.() -> Unit = {}) =
    parent.render().setAsOne(parent, init)