package com.gurunars.floatmenu

import android.animation.ArgbEvaluator
import android.animation.FloatEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.widget.FrameLayout
import com.gurunars.android_utils.IconView
import com.gurunars.android_utils.IconView.Icon
import com.gurunars.databinding.bindableField
import com.gurunars.shortcuts.fullSize

internal class Fab constructor(context: Context) : FrameLayout(context) {

    private val argbEvaluator = ArgbEvaluator()
    private val floatEvaluator = FloatEvaluator()
    private val animatedValue = bindableField(1f)

    val rotationDuration = bindableField(400)
    val openIcon = bindableField(Icon(icon = R.drawable.ic_menu))
    val closeIcon = bindableField(Icon(icon = R.drawable.ic_menu_close))
    val isActivated = bindableField(false)

    private val actualImageView = IconView(context).apply {
        fullSize()
    }

    init {
        addView(actualImageView)

        setOnClickListener { isActivated.set(!isActivated.get()) }

        openIcon.onChange { updateIcon() }
        closeIcon.onChange { updateIcon() }
        animatedValue.onChange { updateIcon() }
        animatedValue.onChange {
            actualImageView.rotation = floatEvaluator.evaluate(it,
                if (isActivated.get()) 0f else 360f,
                if (isActivated.get()) 360f else 0f
            )
        }
        isActivated.onChange {
            if (isAttachedToWindow) {
                ValueAnimator.ofFloat(0f, 1f).apply {
                    startDelay = 0
                    duration = rotationDuration.get().toLong()
                    addUpdateListener { this@Fab.animatedValue.set(it.animatedValue as Float) }
                    start()
                }
            } else {
                updateIcon()
            }
        }
    }

    private fun updateIcon() {
        isClickable = animatedValue.get() == 1f

        // Configs
        val sourceIcon: Icon = if (isActivated.get()) openIcon.get() else closeIcon.get()
        val targetIcon: Icon = if (isActivated.get()) closeIcon.get() else openIcon.get()

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
    }

    override fun onSaveInstanceState(): Parcelable {
        return Bundle().apply {
            putParcelable("superState", super.onSaveInstanceState())
            putBoolean("isActivated", isActivated.get())
        }
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        val localState = state as Bundle
        super.onRestoreInstanceState(localState.getParcelable("superState"))
        isActivated.set(localState.getBoolean("isActivated"))
    }

}
