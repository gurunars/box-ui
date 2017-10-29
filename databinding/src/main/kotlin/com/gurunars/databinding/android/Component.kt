package com.gurunars.databinding.android

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout

interface Component {
    fun Context.render(): View
}

private class ViewWrapper internal constructor(private val view: View): Component {
    override fun Context.render() = view
}

private class LambdaWrapper internal constructor(val renderF: Context.() -> View): Component {
    override fun Context.render(): View = renderF()
}

fun View.wrap(): Component = ViewWrapper(this)

fun component(render: Context.() -> View): Component = LambdaWrapper(render)

fun <T: Component>T.add(parent: ViewGroup, init: View.() -> Unit = {}): T = apply { parent.context.render().add(parent, init) }

fun <T: Component>T.setAsOne(parent: FrameLayout, init: View.() -> Unit = {}): T = apply { parent.context.render().setAsOne(parent, init) }

fun <T: Component>T.setAsOne(parent: Activity, init: View.() -> Unit = {}): T = apply { parent.render().setAsOne(parent, init) }