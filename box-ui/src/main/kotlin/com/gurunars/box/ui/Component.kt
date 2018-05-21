package com.gurunars.box.ui

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout

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

private const val MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT
private const val WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT

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
