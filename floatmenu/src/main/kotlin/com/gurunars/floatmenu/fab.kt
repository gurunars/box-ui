package com.gurunars.floatmenu

import android.animation.ArgbEvaluator
import android.animation.FloatEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.view.View
import com.gurunars.android_utils.Icon
import com.gurunars.android_utils.iconView
import com.gurunars.box.Box
import com.gurunars.box.IBox
import com.gurunars.box.IRoBox
import com.gurunars.box.box
import com.gurunars.box.ui.add
import com.gurunars.box.ui.fullSize
import com.gurunars.box.ui.onClick
import org.jetbrains.anko.frameLayout

internal fun Context.fab(
    rotationDuration: Int,
    openIcon: IRoBox<Icon>,
    closeIcon: IRoBox<Icon>,
    isActivated: IBox<Boolean>
): View = frameLayout {
    val argbEvaluator = ArgbEvaluator()
    val floatEvaluator = FloatEvaluator()
    val animatedValueCache = Box(1f)

    val icon = openIcon.get().box

    val actualImageView = iconView(icon = icon).add(this) {
        id = R.id.iconView
        fullSize()
    }

    onClick { isActivated.set(!isActivated.get()) }

    fun updateIcon(sourceIcon: Icon, targetIcon: Icon, animatedValue: Float) {
        animatedValueCache.set(animatedValue)
        isClickable = animatedValue == 1f
        icon.set(Icon(
            bgColor = argbEvaluator.evaluate(
                animatedValue,
                sourceIcon.bgColor,
                targetIcon.bgColor) as Int,
            fgColor = argbEvaluator.evaluate(
                animatedValue,
                sourceIcon.fgColor,
                targetIcon.fgColor) as Int,
            icon = if (animatedValue < 0.5f) sourceIcon.icon else targetIcon.icon
        ))
        actualImageView.rotation = floatEvaluator.evaluate(animatedValue,
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

    fun getSourceIcon() = if (isActivated.get()) openIcon.get() else closeIcon.get()
    fun getTargetIcon() = if (isActivated.get()) closeIcon.get() else openIcon.get()

    fun updateIconInstantly() =
        updateIcon(
            sourceIcon = getSourceIcon(),
            targetIcon = getTargetIcon(),
            animatedValue = animatedValueCache.get()
        )

    fun IRoBox<Icon>.onChangeWithPreviousIcon(
        shouldBeActive: Boolean
    ) {
        val previousIcon = get().box
        onChange {
            if (isAttachedToWindow && isClickable && isActivated.get() == shouldBeActive) {
                triggerAnimation(previousIcon.get(), it)
            } else {
                updateIconInstantly()
            }
            previousIcon.set(it)
        }
    }

    openIcon.onChangeWithPreviousIcon(false)
    closeIcon.onChangeWithPreviousIcon(true)

    isActivated.onChange {
        if (isAttachedToWindow) {
            triggerAnimation(sourceIcon = getSourceIcon(), targetIcon = getTargetIcon())
        } else {
            updateIconInstantly()
        }
    }
}
