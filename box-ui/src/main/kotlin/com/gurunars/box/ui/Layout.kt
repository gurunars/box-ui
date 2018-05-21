package com.gurunars.box.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity

fun layoutParams() = ViewGroup.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)

fun ViewGroup.LayoutParams.asColumn() {
    width = WRAP_CONTENT
    height = MATCH_PARENT
}

/**
 * width = MATCH_PARENT
 * height = MATCH_PARENT
 */
fun ViewGroup.LayoutParams.matchParent() {
    width = MATCH_PARENT
    height = MATCH_PARENT
}

/**
 * width = MATCH_PARENT
 * height = WRAP_CONTENT
 */
fun ViewGroup.LayoutParams.asRow() {
    width = MATCH_PARENT
    height = WRAP_CONTENT
}

/**
 * width = WRAP_CONTENT
 * height = WRAP_CONTENT
 */
fun ViewGroup.LayoutParams.wrapContent() {
    width = WRAP_CONTENT
    height = WRAP_CONTENT
}

/**
 * width = MATCH_PARENT
 * height = MATCH_PARENT
 */
fun View.matchParent() {
    layoutParams = layoutParams ?: ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
    layoutParams.matchParent()
}

/**
 * width = MATCH_PARENT
 * height = WRAP_CONTENT
 */
fun View.asRow() {
    layoutParams = layoutParams ?: ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
    layoutParams.asRow()
}


interface WithLayoutParams<T: ViewGroup.LayoutParams> {
    fun <VT: View> VT.layoutParams(config: T.(view: VT) -> Unit = {})
}

class DeclarativeFrameLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
): FrameLayout(context, attrs, defStyleAttr), WithLayoutParams<FrameLayout.LayoutParams> {

    override fun <VT: View> VT.layoutParams(config: LayoutParams.(view: VT) -> Unit) =
            addView(this, LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
                config(this@layoutParams)
            })

}


class DeclarativeLinearLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
): LinearLayout(context, attrs, defStyleAttr), WithLayoutParams<LinearLayout.LayoutParams> {

    override fun <VT: View> VT.layoutParams(config: LayoutParams.(view: VT) -> Unit) =
        addView(this, LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
            config(this@layoutParams)
        })

}


class DeclarativeRelativeLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
): RelativeLayout(context, attrs, defStyleAttr), WithLayoutParams<RelativeLayout.LayoutParams> {

    override fun <VT: View> VT.layoutParams(config: LayoutParams.(view: VT) -> Unit) =
        addView(this, LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
            config(this@layoutParams)
        })

}


fun Context.horizontalLayout(init: DeclarativeLinearLayout.() -> Unit = {}) = DeclarativeLinearLayout(this).apply {
    orientation = LinearLayout.HORIZONTAL
    init()
}

fun Context.verticalLayout(init: DeclarativeLinearLayout.() -> Unit = {}) = DeclarativeLinearLayout(this).apply {
    orientation = LinearLayout.VERTICAL
    init()
}

var LinearLayout.gravity_ by ConsumerField<LinearLayout, Int> { gravity = it }

fun Context.relativeLayout(init: DeclarativeRelativeLayout.() -> Unit = {}) = DeclarativeRelativeLayout(this).apply { init() }

fun Context.frameLayout(init: DeclarativeFrameLayout.() -> Unit = {}) = DeclarativeFrameLayout(this).apply { init() }

open class BaseDeclarativeActivity: AppCompatActivity() {
    fun <T : View> T.layoutParams(config: ViewGroup.LayoutParams.() -> Unit): T = this.also { view ->
        addContentView(
            view,
            ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT).apply {
                config()
            }
        )
    }
}

var ViewGroup.MarginLayoutParams.margin by PlainConsumerField<ViewGroup.MarginLayoutParams, Bounds> {
    bottomMargin = it.bottom
    topMargin = it.top
    leftMargin = it.left
    rightMargin = it.right
}

var ViewGroup.MarginLayoutParams.margin_ by ConsumerField<ViewGroup.MarginLayoutParams, Bounds> {
    margin = it
}

fun <T: ViewGroup.LayoutParams>T.update(updater: T.() -> Unit) = apply {
    updater()
}