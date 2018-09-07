package com.gurunars.functional

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.support.annotation.DrawableRes
import android.util.SparseArray
import android.view.ViewGroup

import android.view.View as AndroidView
import android.graphics.drawable.Drawable as AndroidDrawable
import android.view.Gravity as AndroidGravity
import android.graphics.Color as AndroidColor

/**
 * @property left
 * @property right
 * @property top
 * @property bottom
 */
data class Bounds(
    val left: Size = 0.dp,
    val right: Size = 0.dp,
    val top: Size = 0.dp,
    val bottom: Size = 0.dp
) {
    /** Padding for all dimensions */
    constructor(all: Size = 0.dp) : this(all, all)

    /** Padding for horizontal and vertical dimensions */
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
    LEFT(AndroidGravity.LEFT),
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

interface LayoutParams {
    val width: Size
    val height: Size
}

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

/**
 * Pivot:
 * - setPivotX(x: Float)
 * - setPivotY(y: Float)
 *
 * Scale:
 * - setScaleX(x: Float);
 * - setScaleY(y: Float);
 *
 * Translation:
 * - setTranslationX(tx);
 * - setTranslationY(ty);
 * - setTranslationZ(tz);
 * - setElevation(elevation);
 *
 * Rotation:
 * - setRotation
 * - setRotationX
 * - setRotationY
 * - setCameraDistance
 *
 * Elevation:
 * - setElevation
 */
data class Animation(
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

data class View(
    val child: Any,
    val layoutParams: LayoutParams,
    val padding: Bounds = Bounds(0.dp),
    val background: Background? = null,
    val foreground: Foreground? = null,
    val tags: SparseArray<String>? = SparseArray(),
    val visibility: Visibility = Visibility.VISIBLE,
    val flags: Flags = Flags(),
    val animation: Animation = Animation()
)

class ViewBinder<LayoutParamsT: ViewGroup.LayoutParams>(
    private val childBinder: ElementBinder,
    private val paramBinder: Binder<LayoutParams, LayoutParamsT>
): ElementBinder {

    override val empty = View(
        child=childBinder.empty,
        layoutParams=paramBinder.empty
    )

    override fun getEmptyTarget(context: Context): AndroidView =
        childBinder.getEmptyTarget(context)

    private fun Context.getAndroidDrawable(drawable: Drawable?) =
        when (drawable) {
            is DrawableRef ->  getDrawable(drawable.drawableRes)
            is DrawableValue -> drawable.drawable
            else -> null
        }

    private val changeSpec = listOf<ChangeSpec<View, *, AndroidView>>(
        { it: View -> it.padding } rendersTo { setPadding(
            context.toInt(it.left),
            context.toInt(it.top),
            context.toInt(it.right),
            context.toInt(it.bottom)
        ) },
        { it: View -> it.tags } rendersTo {
            it?.let {
                (0 until it.size()).forEach { i ->
                    setTag(it.keyAt(i), it.valueAt(i))
                }
            }
        },
        { it: View -> it.visibility } rendersTo {
            visibility = it.value
        },
        { it: View -> it.background?.drawable } rendersTo {
            background = context.getAndroidDrawable(it)
        },
        { it: View -> it.background?.tintMode } rendersTo {
            backgroundTintMode = it
        },
        { it: View -> it.background?.tint } rendersTo {
            backgroundTintList = it
        },
        { it: View -> it.foreground?.drawable } rendersTo {
            foreground = context.getAndroidDrawable(it)
        },
        { it: View -> it.foreground?.tintMode } rendersTo {
            foregroundTintMode = it
        },
        { it: View -> it.foreground?.tint } rendersTo {
            foregroundTintList = it
        },
        { it: View -> it.foreground?.gravity } rendersTo {
            foregroundGravity = null ?: AndroidGravity.START and AndroidGravity.TOP
        },
        { it: View -> it.flags.activated } rendersTo {
            isActivated = it
        },
        { it: View -> it.flags.clickable } rendersTo {
            isClickable = it
        },
        { it: View -> it.flags.enabled } rendersTo {
            isEnabled = it
        },
        { it: View -> it.flags.selected } rendersTo {
            isSelected = it
        },
        { it: View -> it.flags.focusable } rendersTo {
            isFocusable = it
        },
        { it: View -> it.animation.pivot.x } rendersTo {
            pivotX = it
        },
        { it: View -> it.animation.pivot.y } rendersTo {
            pivotY = it
        },
        { it: View -> it.animation.scale.x } rendersTo {
            scaleX = it
        },
        { it: View -> it.animation.scale.y } rendersTo {
            scaleY = it
        },
        { it: View -> it.animation.scale.y } rendersTo {
            scaleY = it
        },
        { it: View -> it.animation.translation.x } rendersTo {
            translationX = it
        },
        { it: View -> it.animation.translation.y } rendersTo {
            translationY = it
        },
        { it: View -> it.animation.translation.z } rendersTo {
            translationZ = it
        },
        { it: View -> it.animation.elevation } rendersTo {
            elevation = it
        },
        { it: View -> it.animation.alpha } rendersTo {
            alpha = it
        },
        { it: View -> it.animation.rotation.cameraDistance } rendersTo {
            cameraDistance = it
        },
        { it: View -> it.animation.rotation.general } rendersTo {
            rotation = it
        },
        { it: View -> it.animation.rotation.x } rendersTo {
            rotationX = it
        },
        { it: View -> it.animation.rotation.y } rendersTo {
            rotationY = it
        }
    )

    override fun diff(old: Any, new: Any): List<Mutation> =
        changeSpec.diff(old as View, new as View) +
        paramBinder.diff(old.layoutParams, new.layoutParams) +
        childBinder.diff(old.child, new.child)

}