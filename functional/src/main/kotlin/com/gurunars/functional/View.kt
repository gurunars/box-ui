package com.gurunars.functional

import android.content.Context
import android.content.res.ColorStateList
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

enum class GravityOptions(
    val value: Int
) {
    LEFT(AndroidGravity.LEFT), RIGHT(AndroidGravity.RIGHT), CENTER(AndroidGravity.CENTER)
}

typealias Gravity = Set<GravityOptions>

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
    val tintMode: Int
)

data class Foreground(
    val drawable: Drawable,
    val tint: ColorStateList,
    val tintMode: Int,
    val gravity: Gravity
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

enum class Visibility(val visibility: Int) {
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

    private val changeSpec = listOf<ChangeSpec<View, *, AndroidView>>(
        { it: View -> it.layoutParams } rendersTo { this }
    )

    override fun diff(old: Any, new: Any): List<Mutation> =
        changeSpec.diff(old as View, new as View) +
        paramBinder.diff(old.layoutParams, new.layoutParams)

}