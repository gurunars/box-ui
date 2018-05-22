package com.gurunars.box.ui

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT

interface Component {
    fun Context.render(): View
}

fun <Layout: ViewGroup, LayoutParams: ViewGroup.LayoutParams> Component.layout(
    container: Layout,
    config: LayoutParams.() -> Unit,
    defaultConfig: LayoutParams
) = with(container.context) {
    container.addView(render().apply {
        layoutParams = defaultConfig.apply {
            config()
        }
    })
}

fun Component.layout(
    frameLayout: FrameLayout,
    config: FrameLayout.LayoutParams.() -> Unit
) = layout(frameLayout, config, FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT))

fun Component.layout(
    linearLayout: LinearLayout,
    config: LinearLayout.LayoutParams.() -> Unit
) = layout(linearLayout, config, LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT))

fun Component.layout(
    relativeLayout: RelativeLayout,
    config: RelativeLayout.LayoutParams.() -> Unit
) = layout(relativeLayout, config, RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT))

inline fun <reified T: View> Context.of(viewClass: Class<T>): T =
    viewClass.getConstructor(Context::class.java).newInstance(this)
