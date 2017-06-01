package com.gurunars.floatmenu

import android.animation.ArgbEvaluator
import android.animation.FloatEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.InsetDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.content.res.ResourcesCompat
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import com.gurunars.android_utils.AutoBg
import com.gurunars.android_utils.ColoredShapeDrawable
import com.gurunars.databinding.bindableField
import org.jetbrains.anko.matchParent

internal class Fab constructor(context: Context) : FrameLayout(context) {

    private val argbEvaluator = ArgbEvaluator()
    private val floatEvaluator = FloatEvaluator()
    private val animatedValue = bindableField(1f)
    private var withAnimation = false

    val rotationDuration = bindableField(400)
    val openIcon = bindableField(Icon(icon = R.drawable.ic_menu))
    val closeIcon = bindableField(Icon(icon = R.drawable.ic_menu_close))
    val isActivated = bindableField(false)

    private val actualImageView = ImageView(context).apply {
        layoutParams = LayoutParams(matchParent, matchParent)
    }

    init {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        addView(actualImageView)

        setOnClickListener {
            isActivated.set(!isActivated.get())
        }

        openIcon.bind { updateIcon() }
        closeIcon.bind { updateIcon() }
        animatedValue.bind { updateIcon() }
        isActivated.bind {
            if (withAnimation) {
                ValueAnimator.ofFloat(0f, 1f).apply {
                    startDelay = 0
                    duration = rotationDuration.get().toLong()
                    addUpdateListener { this@Fab.animatedValue.set(it.animatedValue as Float) }
                    start()
                }
            }
            updateIcon()
        }
        withAnimation = true
    }

    private fun updateIcon() {
        isClickable = animatedValue.get() == 1f

        // Configs
        val sourceIcon: Icon = if (isActivated.get()) openIcon.get() else closeIcon.get()
        val targetIcon: Icon = if (isActivated.get()) closeIcon.get() else openIcon.get()

        val currentIcon = Icon(
                bgColor=argbEvaluator.evaluate(
                        animatedValue.get(),
                        sourceIcon.bgColor,
                        targetIcon.bgColor) as Int,
                fgColor=argbEvaluator.evaluate(
                        animatedValue.get(),
                        sourceIcon.fgColor,
                        targetIcon.fgColor) as Int,
                icon=if (animatedValue.get() < 0.5f) sourceIcon.icon else targetIcon.icon
        )

        // Bg
        background = ColoredShapeDrawable(OvalShape(), currentIcon.bgColor)
        AutoBg.apply(this, 6)

        // View
        actualImageView.setImageDrawable(InsetDrawable(
                ResourcesCompat.getDrawable(resources, currentIcon.icon, null)?.apply {
                    setColorFilter(currentIcon.fgColor, PorterDuff.Mode.SRC_IN)
                }, 100
        ))
        actualImageView.rotation = floatEvaluator.evaluate(animatedValue.get(),
                if (isActivated.get()) 0f else 360f,
                if (isActivated.get()) 360f else 0f
        )

        // Content description
        contentDescription = "|BG:" + currentIcon.bgColor +
                "|IC:" + currentIcon.fgColor +
                "|ACT:" + isActivated

        requestLayout()
    }

    override fun onSaveInstanceState(): Parcelable {
        return Bundle().apply {
            putParcelable("superState", super.onSaveInstanceState())
            putBoolean("isActivated", isActivated.get())
        }
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        withAnimation = false
        val localState = state as Bundle
        super.onRestoreInstanceState(localState.getParcelable("superState"))
        isActivated.set(localState.getBoolean("isActivated"))
        withAnimation = true
    }

}
