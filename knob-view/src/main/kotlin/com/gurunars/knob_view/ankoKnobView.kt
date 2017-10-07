package com.gurunars.knob_view

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewManager


import kotlin.Unit
import org.jetbrains.anko.custom.ankoView

fun ViewManager.knobView(viewSelector: Map<Enum<*>, View>, init: KnobView.() -> Unit): KnobView = ankoView({ KnobView(it, viewSelector) }, 0, init)
fun Activity.knobView(viewSelector: Map<Enum<*>, View>, init: KnobView.() -> Unit): KnobView = ankoView({ KnobView(it, viewSelector) }, 0, init)
fun Context.knobView(viewSelector: Map<Enum<*>, View>, init: KnobView.() -> Unit): KnobView = ankoView({ KnobView(it, viewSelector) }, 0, init)