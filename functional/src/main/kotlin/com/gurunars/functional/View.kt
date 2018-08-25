package com.gurunars.functional

interface LayoutParams {
    val width: Size
    val height: Size
}

/*

Rationale: provide a View class that is simple. Even primitive.

But also provide means of implementing custom widgets that add necessary
property support.


setFocusable
setSelected
setActivated
setAlpha
setClickable
setElevation
setEnabled
setTag
setVisibility (enum)

interface: Drawable {
    val drawable: AndroidDrawable,
    val tint: ColorStateList,
    val tintMode: Int
}

data class Background(
    ...
): Drawable

background: Background

setBackground
setBackgroundTint
setBackgroundTintMode

data class Foreground(
    val gravity: ...
): Drawable

foreground: Foreground

setForeground
setForegroundGravity
setForegroundTint
setForegroundTintMode

# Animation

startAnimation

setPivotY
setPivotX
setRotation
setRotationX
setRotationY
setCameraDistance
setScaleX
setScaleY
setTranslationX + setX
setTranslationY + setY
setTranslationZ + setZ

*/
data class View<LayoutParamsT: LayoutParams>(
    val child: Any,
    val layoutParams: LayoutParamsT,
    val padding: Bounds = Bounds()

)