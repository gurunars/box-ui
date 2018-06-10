package com.gurunars.floatmenu

import android.animation.ArgbEvaluator
import android.animation.FloatEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.gurunars.android_utils.Icon
import com.gurunars.android_utils.iconView
import com.gurunars.box.*
import com.gurunars.box.ui.*
import com.gurunars.box.ui.decorators.clickThroughLayer
import com.gurunars.box.ui.decorators.statefulLayer


private fun Context.fab(
    rotationDuration: Int,
    openIcon: Icon,
    closeIcon: Icon,
    isActivated: IBox<Boolean>
): View = with<FrameLayout> {
    val argbEvaluator = ArgbEvaluator()
    val floatEvaluator = FloatEvaluator()
    val animatedValueCache = Box(1f)

    val icon = openIcon.box

    val actualImageView = iconView(icon = icon).layout(this) {
        id = R.id.iconView
        fullSize()
    }

    onClick { isActivated.set(!isActivated.get()) }

    fun updateIcon(sourceIcon: Icon, targetIcon: Icon, animatedValue: Float) {
        animatedValueCache.set(animatedValue)
        isClickable = animatedValue == 1f
        icon.set(
            Icon(
                bgColor = argbEvaluator.evaluate(
                    animatedValue,
                    sourceIcon.bgColor,
                    targetIcon.bgColor
                ) as Int,
                fgColor = argbEvaluator.evaluate(
                    animatedValue,
                    sourceIcon.fgColor,
                    targetIcon.fgColor
                ) as Int,
                icon = if (animatedValue < 0.5f) sourceIcon.icon else targetIcon.icon
            )
        )
        actualImageView.rotation = floatEvaluator.evaluate(
            animatedValue,
            if (isActivated.get()) 0f else 360f,
            if (isActivated.get()) 360f else 0f
        )
    }

    fun triggerAnimation(sourceIcon: Icon, targetIcon: Icon) {
        ValueAnimator.ofFloat(0f, 1f).apply {
            startDelay = 0
            duration = rotationDuration.toLong()
            addUpdateListener { updateIcon(sourceIcon, targetIcon, it.animatedValue as Float) }
            start()
        }
    }

    fun getSourceIcon() = if (isActivated.get()) openIcon else closeIcon
    fun getTargetIcon() = if (isActivated.get()) closeIcon else openIcon

    fun updateIconInstantly() =
        updateIcon(
            sourceIcon = getSourceIcon(),
            targetIcon = getTargetIcon(),
            animatedValue = animatedValueCache.get()
        )

    isActivated.onChange {
        if (isAttachedToWindow) {
            triggerAnimation(sourceIcon = getSourceIcon(), targetIcon = getTargetIcon())
        } else {
            updateIconInstantly()
        }
    }
}


/**
 * Floating menu available via a
 * [FAB](https://material.google.com/components/buttons-floating-action-button.html)
 *
 * @param animationDuration Time it takes to perform all the animated UI transitions.
 * @param openIcon icon visible when the menu is closed
 * @param closeIcon icon visible when the menu is open
 * @param isOpen flag indicating visibility with the menu pane on the screen
 */
fun Context.floatMenu(
    animationDuration: Int = 400,
    openIcon: Icon,
    closeIcon: Icon,
    isOpen: IBox<Boolean> = Box(false),
    menuSupplier: () -> View
) = statefulLayer(R.id.floatMenu) {
    retain(isOpen)
    with<RelativeLayout> {

        val animatedValue = Box(1f)

        isOpen.onChange { visible ->
            if (isAttachedToWindow) {
                ValueAnimator.ofFloat(
                    if (visible) 0f else 1f,
                    if (visible) 1f else 0f
                ).apply {
                    startDelay = 0
                    duration = animationDuration.toLong()
                    addUpdateListener {
                        animatedValue.set(it.animatedValue as Float)
                    }
                    start()
                }
            } else {
                animatedValue.set(0f)
                animatedValue.broadcast()
            }
        }

        clickThroughLayer {
            menuSupplier().apply {
                animatedValue.onChange {
                    alpha = it
                    setIsVisible(isOpen.get() && it != 0f)
                }
            }
        }.layout(this) {
            fullSize()
        }

        fab(
            animationDuration,
            openIcon,
            closeIcon,
            isOpen
        ).apply {
            id = R.id.openFab
        }.layout(this) {
            width = dip(60)
            height = dip(60)
            alignInParent()
            alignInParent(HorizontalAlignment.RIGHT, VerticalAlignment.BOTTOM)
            margin = Bounds(dip(16))
        }
    }
}
