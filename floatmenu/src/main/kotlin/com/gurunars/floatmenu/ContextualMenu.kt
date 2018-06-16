package com.gurunars.floatmenu

import android.animation.FloatEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.gurunars.android_utils.Icon
import com.gurunars.android_utils.iconView
import com.gurunars.box.*
import com.gurunars.box.ui.*
import com.gurunars.box.ui.layers.clickThroughLayer
import com.gurunars.box.ui.layers.statefulLayer


private fun Context.fab(
    rotationDuration: Int,
    closeIconColors: Icon.Colors,
    isActivated: IBox<Boolean>
): View = with<FrameLayout> {
    val floatEvaluator = FloatEvaluator()
    val animatedValueCache = Box(1f)

    val actualImageView = iconView(
        icon = closeIconColors.icon(R.drawable.ic_check).box
    ).layout(this) {
        id = R.id.iconView
        gravity = Gravity.CENTER
        width = dip(0)
        height = dip(0)
    }

    fun setSize(percentageOfMax: Float) {
        val parentWidth = width
        val parentHeight = height
        actualImageView.layoutParams.apply {
            width = (parentWidth * percentageOfMax).toInt()
            height = (parentHeight * percentageOfMax).toInt()
        }
    }

    onClick { isActivated.set(!isActivated.get()) }

    fun updateIcon(animatedValue: Float) {
        animatedValueCache.set(animatedValue)
        //isClickable = animatedValue == 1f
        setSize(animatedValue)
        actualImageView.rotation = floatEvaluator.evaluate(
            animatedValue,
            0f,
            360f
        )
    }

    fun triggerAnimation() {
        val active = isActivated.get()
        ValueAnimator.ofFloat(
            if(active) 0f else 1f,
            if(active) 1f else 0f
        ).apply {
            startDelay = 0
            duration = rotationDuration.toLong()
            addUpdateListener { updateIcon(it.animatedValue as Float) }
            start()
        }
    }

    fun updateIconInstantly() =
        updateIcon(
            animatedValue = animatedValueCache.get()
        )

    isActivated.onChange {
        if (isAttachedToWindow) {
            triggerAnimation()
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
 * @param closeIconColors icon visible when the menu is open
 * @param isOpen flag indicating visibility with the menu pane on the screen
 */
fun Context.contextualMenu(
    animationDuration: Int = 400,
    closeIconColors: Icon.Colors = Icon.Colors(),
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
            closeIconColors,
            isOpen
        ).apply {
            id = R.id.openFab
        }.layout(this) {
            width = dip(60)
            height = dip(60)
            alignInParent()
            alignInParent(HorizontalAlignment.RIGHT, VerticalAlignment.BOTTOM)
            margin = OldBounds(dip(16))
        }
    }
}
