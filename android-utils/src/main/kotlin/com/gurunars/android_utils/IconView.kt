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
import android.support.v4.content.res.ResourcesCompat
import android.widget.ImageView
import com.gurunars.anko_generator.AnkoComponent
import com.gurunars.databinding.BindableField

/**
 * Button with customizable icon, background and foreground color.
 *
 * @property icon Button icon descriptor
 * @property enabled Flag specifying if the icon should be clickable or not
 */
@AnkoComponent
class IconView(context: Context) : ImageView(context) {

    /**
     * Data class holding the values to be used for icon drawable creation
     *
     * @property bgColor background color integer
     * @property fgColor icon color integer
     * @property icon drawable resource ID
     * @property shape shape of the drawable
     */
    data class Icon(
        val bgColor: Int = Color.RED,
        val fgColor: Int = Color.WHITE,
        val icon: Int,
        val shape: Shape = OvalShape()
    )

    val icon = BindableField(Icon(
        bgColor = Color.WHITE,
        fgColor = Color.BLACK,
        icon = R.drawable.ic_plus
    ))

    val enabled = BindableField(true)

    private lateinit var iconDrawable: Drawable

    init {
        icon.onChange {
            reset(it, enabled.get())
        }
        enabled.onChange {
            reset(icon.get(), it)
        }
    }

    private fun reset(currentIcon: Icon, enabled: Boolean) {
        val shadowWidth = 4
        val inset = 50

        isEnabled = enabled

        background = ColoredShapeDrawable(currentIcon.shape, currentIcon.bgColor)
        setAutoBg(shadowWidth)

        iconDrawable = InsetDrawable(
            ResourcesCompat.getDrawable(resources, currentIcon.icon, null)?.apply {
                setColorFilter(currentIcon.fgColor, PorterDuff.Mode.SRC_IN)
            }, inset
        )

        // Content description
        contentDescription = "|BG:" + currentIcon.bgColor +
            "|IC:" + currentIcon.fgColor

        setImageDrawable(iconDrawable)
    }

    private fun getRotateDrawable(d: Drawable, angle: Float): Drawable {
        return object : LayerDrawable(arrayOf(d)) {
            override fun draw(canvas: Canvas) {
                canvas.apply {
                    save()
                    fun Int.bound() = toFloat() / 2
                    rotate(angle, d.bounds.width().bound(), d.bounds.height().bound())
                    super.draw(this)
                    restore()
                }
            }
        }
    }

    /**
     * @suppress
     */
    override fun setRotation(rotation: Float) {
        setImageDrawable(getRotateDrawable(iconDrawable, rotation))
    }

}
