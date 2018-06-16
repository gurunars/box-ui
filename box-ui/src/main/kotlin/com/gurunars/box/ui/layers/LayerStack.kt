package com.gurunars.box.ui.layers

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import com.gurunars.box.ui.fullSize
import com.gurunars.box.ui.with
import com.gurunars.box.ui.layout

fun Context.layerStack(vararg layers: View) =
    with<FrameLayout> {
        layers.forEach {
            it.layout(this) {
                fullSize()
            }
        }
    }