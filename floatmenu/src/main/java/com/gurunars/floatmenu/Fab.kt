package com.gurunars.floatmenu

import android.animation.*
import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.InsetDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.content.res.ResourcesCompat
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import com.gurunars.android_utils.AutoBg
import com.gurunars.android_utils.ColoredShapeDrawable

internal class Fab constructor(context: Context) : FrameLayout(context) {

    private var openIcon: Icon = Icon(icon = R.drawable.ic_menu)
    private var closeIcon: Icon = Icon(icon = R.drawable.ic_menu_close)
    private var currentIcon: Icon = openIcon
    private var iconRotationAngle: Float = 0f

    internal var rotationDuration = 400

    private val actualImageView: ImageView
    private val argbEvaluator = ArgbEvaluator()
    private val floatEvaluator = FloatEvaluator()

    init {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        actualImageView = ImageView(getContext())
        actualImageView.layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
        addView(actualImageView)
        loadFromState()
    }

    private fun reloadUi() {
        // Bg
        background = ColoredShapeDrawable(OvalShape(), currentIcon.bgColor)
        AutoBg.apply(this, 6)
        // Icon
        val fg = ResourcesCompat.getDrawable(resources, currentIcon.icon, null)
        fg?.setColorFilter(currentIcon.fgColor, PorterDuff.Mode.SRC_IN)
        actualImageView.setImageDrawable(InsetDrawable(fg, ICON_PADDING))
        actualImageView.rotation = iconRotationAngle
        // Content description
        contentDescription = "|BG:" + currentIcon.bgColor +
                "|IC:" + currentIcon.fgColor +
                "|ACT:" + isActivated
        requestLayout()
    }

    fun setRotationDuration(durationInMillis: Int) {
        this.rotationDuration = durationInMillis
    }

    fun setOpenIcon(icon: Icon) {
        this.openIcon = icon
        loadFromState()
    }

    fun setCloseIcon(icon: Icon) {
        this.closeIcon = icon
        loadFromState()
    }

    private fun loadFromState() {
        currentIcon = if (isActivated) closeIcon else openIcon
        iconRotationAngle = (if (isActivated) 360 else 0).toFloat()
        reloadUi()
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
        loadFromState()
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
                val animatedValue = it.animatedValue as Float

                val sourceIcon: Icon = if (isActive) openIcon else closeIcon
                val targetIcon: Icon = if (isActive) closeIcon else openIcon

                val bgColor = argbEvaluator.evaluate(animatedValue, sourceIcon.bgColor, targetIcon.bgColor) as Int
                val fgColor = argbEvaluator.evaluate(animatedValue, sourceIcon.fgColor, targetIcon.fgColor) as Int
                val icon = if (animatedValue < 0.5f) sourceIcon.icon else targetIcon.icon

                iconRotationAngle = floatEvaluator.evaluate(animatedValue,
                        if (isActive) 0f else 360f,
                        if (isActive) 360f else 0f
                )

                currentIcon = currentIcon.copy(bgColor=bgColor, fgColor=fgColor, icon=icon)

                reloadUi()
            }
            start()
        }
    }

    companion object {

        private val ICON_PADDING = 100
    }
}
