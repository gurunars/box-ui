package com.gurunars.floatmenu

import android.animation.ArgbEvaluator
import android.animation.FloatEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.view.View
import com.gurunars.android_utils.Icon
import com.gurunars.android_utils.iconView
import com.gurunars.box.*
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
    val animatedValue = Box(1f)

    val icon = openIcon.get().box

    val actualImageView = iconView(icon = icon).add(this) {
        id = R.id.iconView
        fullSize()
    }

    onClick { isActivated.set(!isActivated.get()) }

    fun updateIcon() {
        isClickable = animatedValue.get() == 1f

        // Configs
        val sourceIcon: Icon = if (isActivated.get()) openIcon.get() else closeIcon.get()
        val targetIcon: Icon = if (isActivated.get()) closeIcon.get() else openIcon.get()

        icon.set(Icon(
            bgColor = argbEvaluator.evaluate(
                animatedValue.get(),
                sourceIcon.bgColor,
                targetIcon.bgColor) as Int,
            fgColor = argbEvaluator.evaluate(
                animatedValue.get(),
                sourceIcon.fgColor,
                targetIcon.fgColor) as Int,
            icon = if (animatedValue.get() < 0.5f) sourceIcon.icon else targetIcon.icon
        ))
        actualImageView.rotation = floatEvaluator.evaluate(animatedValue.get(),
            if (isActivated.get()) 0f else 360f,
            if (isActivated.get()) 360f else 0f
        )
    }

    onChange(openIcon, closeIcon, animatedValue) {
        updateIcon()
    }

    isActivated.onChange { _ ->
        if (isAttachedToWindow) {
            ValueAnimator.ofFloat(0f, 1f).apply {
                startDelay = 0
                duration = rotationDuration.toLong()
                addUpdateListener { animatedValue.set(it.animatedValue as Float) }
                start()
            }
        } else {
            updateIcon()
        }
    }
}
