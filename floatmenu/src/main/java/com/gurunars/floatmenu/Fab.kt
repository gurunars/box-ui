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
import org.jetbrains.anko.matchParent

internal class Fab constructor(context: Context) : FrameLayout(context) {

    var openIcon: Icon = Icon(icon = R.drawable.ic_menu)
        set(value) {
            field = value
            reload()
        }
    var closeIcon: Icon = Icon(icon = R.drawable.ic_menu_close)
        set(value) {
            field = value
            reload()
        }

    private var rotationDuration = 400

    private val argbEvaluator = ArgbEvaluator()
    private val floatEvaluator = FloatEvaluator()
    private var animatedValue = 1f

    private val actualImageView = ImageView(context).apply {
        layoutParams = LayoutParams(matchParent, matchParent)
    }

    init {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        addView(actualImageView)
        reload()
    }

    private fun reload() {
        // Configs
        val sourceIcon: Icon = if (isActivated) openIcon else closeIcon
        val targetIcon: Icon = if (isActivated) closeIcon else openIcon

        val currentIcon = Icon(
                bgColor=argbEvaluator.evaluate(animatedValue, sourceIcon.bgColor, targetIcon.bgColor) as Int,
                fgColor=argbEvaluator.evaluate(animatedValue, sourceIcon.fgColor, targetIcon.fgColor) as Int,
                icon=if (animatedValue < 0.5f) sourceIcon.icon else targetIcon.icon
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
        actualImageView.rotation = floatEvaluator.evaluate(animatedValue,
                if (isActivated) 0f else 360f,
                if (isActivated) 360f else 0f
        )

        // Content description
        contentDescription = "|BG:" + currentIcon.bgColor +
                "|IC:" + currentIcon.fgColor +
                "|ACT:" + isActivated

        requestLayout()
    }

    override fun onSaveInstanceState(): Parcelable {
        val bundle = Bundle()
        bundle.putParcelable("superState", super.onSaveInstanceState())
        bundle.putBoolean("isActivated", isActivated)
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        val localState = state as Bundle
        super.onRestoreInstanceState(localState.getParcelable("superState"))
        super.setActivated(localState.getBoolean("isActivated"))
        this.animatedValue = 1f
        reload()
    }

    override fun setActivated(isActive: Boolean) {
        if (isActivated == isActive) {
            return
        }

        super.setActivated(isActive)

        ValueAnimator.ofFloat(0f, 1f).apply {
            startDelay = 0
            duration = rotationDuration.toLong()
            addUpdateListener {
                this@Fab.animatedValue = it.animatedValue as Float
                reload()
            }
            start()
        }
    }

    fun setRotionDuration(duration: Int) {
        this.rotationDuration = duration
    }

}
