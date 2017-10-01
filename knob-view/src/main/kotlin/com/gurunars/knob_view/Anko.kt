package com.gurunars.knob_view

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewManager
import org.jetbrains.anko.custom.ankoView

@Suppress("NOTHING_TO_INLINE")
inline fun Context.knobView(
    viewSelector: Map<Enum<*>, View>
) = KnobView(this, viewSelector)

inline fun ViewManager.knobView(
    viewSelector: Map<Enum<*>, View>,
    init: KnobView.() -> Unit) = ankoView({ KnobView(it, viewSelector) }, 0, init)

inline fun Activity.knobView(
    viewSelector: Map<Enum<*>, View>,
    init: KnobView.() -> Unit) = ankoView({ KnobView(it, viewSelector) }, 0, init)