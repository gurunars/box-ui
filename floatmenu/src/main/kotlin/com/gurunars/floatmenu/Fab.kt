package com.gurunars.floatmenu

import android.animation.ArgbEvaluator
import android.animation.FloatEvaluator
import android.animation.ValueAnimator
import android.content.Context
import com.gurunars.android_utils.IconView
import com.gurunars.android_utils.IconView.Icon
import com.gurunars.databinding.BindableField
import com.gurunars.databinding.android.fullSize
import org.jetbrains.anko.frameLayout

internal fun Context.fab(
    rotationDuration: Int,
    openIcon: Icon,
    closeIcon: Icon,
    animatedValue: BindableField<Float>,
    isActivated: BindableField<Boolean>
) = frameLayout {
    val argbEvaluator = ArgbEvaluator()
    val floatEvaluator = FloatEvaluator()

    val actualImageView = IconView(context).apply {
        id = R.id.iconView
        fullSize()
    }

    addView(actualImageView)

    setOnClickListener { isActivated.set(!isActivated.get()) }

    fun updateIcon() {
        isClickable = animatedValue.get() == 1f

        // Configs
        val sourceIcon: Icon = if (isActivated.get()) openIcon else closeIcon
        val targetIcon: Icon = if (isActivated.get()) closeIcon else openIcon

        actualImageView.icon.set(Icon(
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

    animatedValue.onChange { updateIcon() }
    isActivated.onChange {
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
