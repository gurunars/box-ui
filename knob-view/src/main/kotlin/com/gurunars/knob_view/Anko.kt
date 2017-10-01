package com.gurunars.knob_view

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewManager
import org.jetbrains.anko.custom.ankoView

fun Context.knobView(
    viewSelector: Map<Enum<*>, View>,
    init: KnobView.() -> Unit = {}
) =
    ankoView({ KnobView(it, viewSelector) }, 0, init)

fun ViewManager.knobView(
    viewSelector: Map<Enum<*>, View>,
    init: KnobView.() -> Unit = {}
) =
    ankoView({ KnobView(it, viewSelector) }, 0, init)

fun Activity.knobView(
    viewSelector: Map<Enum<*>, View>,
    init: KnobView.() -> Unit = {}
) =
    ankoView({ KnobView(it, viewSelector) }, 0, init)