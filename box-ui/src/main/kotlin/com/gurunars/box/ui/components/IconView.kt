package com.gurunars.box.ui.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.graphics.drawable.InsetDrawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.graphics.drawable.shapes.Shape
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.res.ResourcesCompat
import com.gurunars.box.core.ObservableValue
import com.gurunars.box.core.IReadOnlyObservableValue
import com.gurunars.box.core.bind
import com.gurunars.box.core.merge
import com.gurunars.box.ui.R
import com.gurunars.box.ui.setAutoBg
import com.gurunars.box.ui.inUi


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

private class IconView(context: Context) : AppCompatImageView(context) {

    val icon = ObservableValue(Icon(
        bgColor = Color.WHITE,
        fgColor = Color.BLACK,
        icon = R.drawable.ic_plus
    ))

    val enabled = ObservableValue(true)

    private lateinit var iconDrawable: Drawable

    init {
        reset(icon.get(), enabled.get())
        merge(icon, enabled).inUi.onChange {
            reset(it.first, it.second)
        }
    }

    private fun reset(currentIcon: Icon, enabled: Boolean) {
        val shadowWidth = 4
        val inset = 50

        isEnabled = enabled

        background = ShapeDrawable(currentIcon.shape).apply {
            paint.color = currentIcon.bgColor
        }
        setAutoBg(shadowWidth)

        iconDrawable = InsetDrawable(
            ResourcesCompat.getDrawable(resources, currentIcon.icon, null)?.apply {
                setColorFilter(currentIcon.fgColor, PorterDuff.Mode.SRC_IN)
            }, inset
        )

        alpha = if (enabled) 1.0f else 0.5f

        contentDescription =
            "|BG:" + currentIcon.bgColor +
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

    /** @suppress */
    override fun setRotation(rotation: Float) {
        setImageDrawable(getRotateDrawable(iconDrawable, rotation))
    }
}

/**
 * Button with customizable icon, background and foreground color.
 *
 * @param icon Button icon descriptor
 * @param enabled Flag specifying if the icon should be clickable or not
 */
fun Context.iconView(
    icon: IReadOnlyObservableValue<Icon> = ObservableValue(Icon(
        bgColor = Color.WHITE,
        fgColor = Color.BLACK,
        icon = R.drawable.ic_plus
    )),
    enabled: IReadOnlyObservableValue<Boolean> = ObservableValue(true)
): View = IconView(this).apply {
    icon.bind(this.icon)
    enabled.bind(this.enabled)
}
