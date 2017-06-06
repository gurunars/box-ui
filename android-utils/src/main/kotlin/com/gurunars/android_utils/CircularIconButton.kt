package com.gurunars.android_utils

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.InsetDrawable
import android.graphics.drawable.shapes.OvalShape
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import com.gurunars.databinding.bindableField
import com.gurunars.shortcuts.setPadding


/**
 * Button with customizable icon, background and foreground color.
 */
class CircularIconButton constructor(context: Context) : ImageButton(context) {

    val icon = bindableField(Icon(
            bgColor = ContextCompat.getColor(context, R.color.White),
            fgColor = ContextCompat.getColor(context, R.color.Black),
            icon = R.drawable.ic_plus
    ))

    val enabled = bindableField(true)

    init {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        isClickable = true
        icon.bind { reset() }
        enabled.bind { reset() }
    }

    private fun reset() {
        background = ColoredShapeDrawable(OvalShape(), icon.get().bgColor)
        AutoBg.apply(this, 4)

        scaleType = ImageView.ScaleType.CENTER_CROP
        setPadding(8)

        val fg = ContextCompat.getDrawable(context, icon.get().icon)
        fg.setColorFilter(icon.get().fgColor, PorterDuff.Mode.SRC_IN)
        fg.alpha = if (isEnabled) 255 else 50
        setImageDrawable(InsetDrawable(fg, 50))
    }

}
