package com.gurunars.functional

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.support.annotation.DrawableRes

import android.view.View as AndroidView
import android.graphics.drawable.Drawable as AndroidDrawable
import android.view.Gravity as AndroidGravity
import android.graphics.Color as AndroidColor

data class Bounds(
    val left: Size = 0.dp,
    val right: Size = 0.dp,
    val top: Size = 0.dp,
    val bottom: Size = 0.dp
) {
    constructor(all: Size = 0.dp) : this(all, all)
    constructor(horizontal: Size = 0.dp, vertical: Size = 0.dp) : this(
        horizontal,
        horizontal,
        vertical,
        vertical
    )
}

data class Color(
    val value: Int
) {
    constructor(value: String): this(AndroidColor.parseColor(value))
}

enum class Gravity(
    val value: Int
) {
    TOP(AndroidGravity.TOP),
    BOTTOM(AndroidGravity.BOTTOM),
    @SuppressLint("RtlHardcoded")
    LEFT(AndroidGravity.LEFT),
    @SuppressLint("RtlHardcoded")
    RIGHT(AndroidGravity.RIGHT),

    CENTER_VERTICAL(AndroidGravity.CENTER_VERTICAL),
    FILL_VERTICAL(AndroidGravity.FILL_VERTICAL),

    CENTER_HORIZONTAL(AndroidGravity.CENTER_HORIZONTAL),
    FILL_HORIZONTAL(AndroidGravity.FILL_HORIZONTAL),

    CENTER(AndroidGravity.CENTER),
    FILL(AndroidGravity.FILL),

    CLIP_VERTICAL(AndroidGravity.CENTER_VERTICAL),
    CLIP_HORIZONTAL(AndroidGravity.CLIP_HORIZONTAL),

    START(AndroidGravity.START),
    END(AndroidGravity.END)
}

fun Set<Gravity>.toInt() =
    fold(AndroidGravity.NO_GRAVITY) { acc, gravity -> acc or gravity.value }

data class TwoDimensional(
    val x: Float = 0f,
    val y: Float = 0f
)

data class ThreeDimensional(
    val x: Float = 0f,
    val y: Float = 0f,
    val z: Float = 0f
)

data class Rotation(
    val general: Float = 0f,
    val x: Float = 0f,
    val y: Float = 0f,
    val cameraDistance: Float = 0f
)

sealed class Drawable

data class DrawableRef(
    @DrawableRes val drawableRes: Int
): Drawable()

data class DrawableValue(
    val drawable: AndroidDrawable
): Drawable()

data class Background(
    val drawable: Drawable,
    val tint: ColorStateList,
    val tintMode: PorterDuff.Mode
)

data class Foreground(
    val drawable: Drawable,
    val tint: ColorStateList,
    val tintMode: PorterDuff.Mode,
    val gravity: Set<Gravity>
)

data class Decoration(
    val pivot: TwoDimensional = TwoDimensional(),
    val scale: TwoDimensional = TwoDimensional(),
    val translation: ThreeDimensional = ThreeDimensional(),
    val rotation: Rotation = Rotation(),
    val elevation: Float = 0f,
    val alpha: Float = 0f
)

enum class Visibility(val value: Int) {
    VISIBLE(AndroidView.VISIBLE), GONE(AndroidView.GONE), INVISIBLE(AndroidView.INVISIBLE)
}

data class Flags(
    val enabled: Boolean = true,
    val clickable: Boolean = false,
    val focusable: Boolean = false,
    val selected: Boolean = false,
    val activated: Boolean = false
)

/**
 * There is a set of operations within views that is intrinsic to their
 * implementation and that does mutate their state. E.g. setFocus.
 *
 * While other attributes exist exclusively in a one way fashion, these
 * ones have a dual nature.
 *
 * The question is how to handle this ones in a coherent manner?
 *
 * The shadow view can't be mutated.
 *
 * The view itself can't have its internal state to be different from
 * the shadow view.
 *
 * Yes we can make it a feedback loop with optional binding between
 * properties, but it does not sound nice.
 */

data class View(
    val child: Any,
    val layoutParams: LayoutParams,
    val padding: Bounds = Bounds(0.dp),
    val background: Background? = null,
    val foreground: Foreground? = null,
    val visibility: Visibility = Visibility.VISIBLE,
    val flags: Flags = Flags(),
    val decoration: Decoration = Decoration(),
    val id: Int = AndroidView.NO_ID
)

class ViewBinder(
    private val childBinder: ElementBinder,
    private val paramBinder: ParamBinder
): ElementBinder {
    override val empty = View(childBinder.empty, paramBinder.empty)

    override fun getEmptyTarget(context: Context): AndroidView =
        childBinder.getEmptyTarget(context)

    private fun Context.getAndroidDrawable(drawable: Drawable?) =
        when (drawable) {
            is DrawableRef ->  getDrawable(drawable.drawableRes)
            is DrawableValue -> drawable.drawable
            else -> null
        }

    private val changeSpec = changeSpecs<View, AndroidView> {
        rendersTo({ padding }){
            setPadding(
                context.toInt(it.left),
                context.toInt(it.top),
                context.toInt(it.right),
                context.toInt(it.bottom)
            )
        }
        rendersTo({ visibility }) {
            visibility = it.value
        }
        rendersTo({ background?.drawable }) {
            background = context.getAndroidDrawable(it)
        }
        rendersTo({ background?.tintMode }) {
            backgroundTintMode = it
        }
        rendersTo({ background?.tint }) {
            backgroundTintList = it
        }
        rendersTo({ foreground?.drawable }) {
            foreground = context.getAndroidDrawable(it)
        }
        rendersTo({ foreground?.tintMode })  {
            foregroundTintMode = it
        }
        rendersTo({ foreground?.tint }) {
            foregroundTintList = it
        }
        rendersTo({ foreground?.gravity ?: setOf(Gravity.TOP, Gravity.LEFT) }) {
            foregroundGravity = it.toInt()
        }
        rendersTo({ flags.activated }) {
            isActivated = it
        }
        rendersTo({ flags.clickable }) {
            isClickable = it
        }
        rendersTo({ flags.enabled }) {
            isEnabled = it
        }
        rendersTo({ flags.selected }) {
            isSelected = it
        }
        rendersTo({ flags.focusable }) {
            isFocusable = it
        }
        rendersTo({ decoration.pivot.x }) {
            pivotX = it
        }
        rendersTo({ decoration.pivot.y }) {
            pivotY = it
        }
        rendersTo({ decoration.scale.x }) {
            scaleX = it
        }
        rendersTo({ decoration.scale.y }) {
            scaleY = it
        }
        rendersTo({ decoration.scale.y }) {
            scaleY = it
        }
        rendersTo({ decoration.translation.x }) {
            translationX = it
        }
        rendersTo({ decoration.translation.y }) {
            translationY = it
        }
        rendersTo({ decoration.translation.z }) {
            translationZ = it
        }
        rendersTo({ decoration.elevation }) {
            elevation = it
        }
        rendersTo({ decoration.alpha }) {
            alpha = it
        }
        rendersTo({ decoration.rotation.cameraDistance }) {
            cameraDistance = it
        }
        rendersTo({ decoration.rotation.general }) {
            rotation = it
        }
        rendersTo({ decoration.rotation.x }) {
            rotationX = it
        }
        rendersTo({ decoration.rotation.y }) {
            rotationY = it
        }
    }

    override fun diff(context: Context, old: Any, new: Any): List<Mutation> =
        changeSpec.diff(old as View, new as View) +
        paramBinder.diff(context, old.layoutParams, new.layoutParams) +
        childBinder.diff(context, old.child, new.child)

}