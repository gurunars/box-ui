package com.gurunars.functional.text

import android.graphics.Paint
import android.graphics.Typeface
import android.widget.TextView
import com.gurunars.functional.*

data class TextAppearance(
    val textStyle: Set<TextStyle> = setOf(),
    val color: Color = Color("#000000"),
    val size: Size = 12.sp,
    val gravity: Gravity = Gravity.LEFT
) {
    enum class TextStyle {
        BOLD, ITALIC, UNDERLINE, STRIKE_THROUGH
    }
}

interface WithTextAppearence {
    val textAppearance: TextAppearance
}

fun<T: WithTextAppearence> textAppearanceChangeSpec() = changeSpecs<T, TextView> {
    rendersTo({ textAppearance.color }) { setTextColor(it.value) }
    rendersTo({ textAppearance.gravity }) { gravity = it.value }
    rendersTo({ textAppearance.size }) { textSize = context.toInt(it).toFloat() }
    rendersTo({ textAppearance.textStyle }) {
        when {
            it.isEmpty()
            -> setTypeface(typeface, Typeface.NORMAL)
            it.containsAll(setOf(TextAppearance.TextStyle.BOLD, TextAppearance.TextStyle.ITALIC))
            -> setTypeface(typeface, Typeface.BOLD_ITALIC)
            it.contains(TextAppearance.TextStyle.BOLD)
            -> setTypeface(typeface, Typeface.BOLD)
            it.contains(TextAppearance.TextStyle.ITALIC)
            -> setTypeface(typeface, Typeface.ITALIC)
        }

        paintFlags = when {
            it.contains(TextAppearance.TextStyle.UNDERLINE)
            -> paintFlags or Paint.UNDERLINE_TEXT_FLAG
            it.contains(TextAppearance.TextStyle.STRIKE_THROUGH)
            -> paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            else
            -> paintFlags
        }
    }
}