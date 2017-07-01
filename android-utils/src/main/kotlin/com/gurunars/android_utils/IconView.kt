package com.gurunars.android_utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.graphics.drawable.InsetDrawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.shapes.OvalShape
import android.graphics.drawable.shapes.Shape
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.widget.ImageView
import com.gurunars.databinding.bindableField


/**
 * Button with customizable icon, background and foreground color.
 */
class IconView constructor(context: Context) : ImageView(context) {

    /**
     * Data class holding the values to be used for icon drawable creation
     *
     * @property bgColor background color integer
     * @property fgColor icon color integer
     * @property icon drawable resource ID
     * @property shape shape of the drawable
     */
    data class Icon(
        val bgColor: Int= Color.RED,
        val fgColor: Int= Color.WHITE,
        val icon: Int,
        val shape: Shape = OvalShape()
    )

    /**
     * Button icon descriptor
     */
    val icon = bindableField(Icon(
        bgColor = ContextCompat.getColor(context, R.color.White),
        fgColor = ContextCompat.getColor(context, R.color.Black),
        icon = R.drawable.ic_plus
    ))

    /**
     * Flag specifying if the icon should be clickable or not
     */
    val enabled = bindableField(true)

    private lateinit var iconDrawable: Drawable

    init {
        icon.onChange { reset(it) }
        enabled.onChange { reset(icon.get()) }
    }

    // TODO: check if enabled flag actually makes any difference
    private fun reset(currentIcon: Icon) {
        val shadowWidth = 4
        val inset = 50

        background = ColoredShapeDrawable(currentIcon.shape, currentIcon.bgColor)
        setAutoBg(this, shadowWidth)

        iconDrawable = InsetDrawable(
            ResourcesCompat.getDrawable(resources, currentIcon.icon, null)?.apply {
                setColorFilter(currentIcon.fgColor, PorterDuff.Mode.SRC_IN)
            }, inset
        )

        // Content description
        contentDescription = "|BG:" + currentIcon.bgColor +
                "|IC:" + currentIcon.fgColor +
                "|ACT:" + isActivated


        setImageDrawable(iconDrawable)
    }

    private fun getRotateDrawable(d: Drawable, angle: Float): Drawable {
        val arD = arrayOf(d)
        return object : LayerDrawable(arD) {
            override fun draw(canvas: Canvas) {
                canvas.save()
                canvas.rotate(angle,
                        d.bounds.width().toFloat() / 2,
                        d.bounds.height().toFloat() / 2)
                super.draw(canvas)
                canvas.restore()
            }
        }
    }

    override fun setRotation(rotation: Float) {
        setImageDrawable(getRotateDrawable(iconDrawable, rotation))
    }

}
