package com.gurunars.android_utils

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.InsetDrawable
import android.graphics.drawable.shapes.OvalShape
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView


/**
 * Button with customizable icon, background and foreground color.
 */
class CircularIconButton constructor(context: Context) : ImageButton(context) {

    internal var backgroundColor: Int = 0
    internal var foregroundColor: Int = 0
    internal var innerDrawable: Int = 0

    init {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        isClickable = true

        backgroundColor = ContextCompat.getColor(context, R.color.White)
        foregroundColor = ContextCompat.getColor(context, R.color.Black)
        innerDrawable = R.drawable.ic_plus

        reset()
    }

    private fun reset() {
        background = ColoredShapeDrawable(OvalShape(), backgroundColor)
        AutoBg.apply(this, 4)

        scaleType = ImageView.ScaleType.CENTER_CROP
        setPadding(8, 8, 8, 8)

        val fg = ContextCompat.getDrawable(context, innerDrawable)
        fg.setColorFilter(foregroundColor, PorterDuff.Mode.SRC_IN)
        fg.alpha = if (isEnabled) 255 else 50
        setImageDrawable(InsetDrawable(fg, ICON_PADDING))
    }

    fun setInnerDrawable(innerDrawable: Int) {
        this.innerDrawable = innerDrawable
        reset()
    }

    override fun setBackgroundColor(backgroundColor: Int) {
        this.backgroundColor = backgroundColor
        reset()
    }

    fun setForegroundColor(foregroundColor: Int) {
        this.foregroundColor = foregroundColor
        reset()
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        reset()
    }

    companion object {

        private val ICON_PADDING = 50
    }
}
