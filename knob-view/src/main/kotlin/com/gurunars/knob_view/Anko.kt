package com.gurunars.knob_view

import android.app.Activity
import android.view.View
import android.view.ViewManager
import org.jetbrains.anko.custom.ankoView

/**
 * Anko specific view function for KnobView
 *
 * @see KnobView
 */
inline fun ViewManager.knobView(
    viewSelector: Map<Enum<*>, View>,
    init: KnobView.() -> Unit) = ankoView({ KnobView(it, viewSelector) }, 0, init)

/**
 * Anko specific view function for KnobView
 *
 * @see KnobView
 */
inline fun Activity.knobView(
    viewSelector: Map<Enum<*>, View>,
    init: KnobView.() -> Unit) = ankoView({ KnobView(it, viewSelector) }, 0, init)