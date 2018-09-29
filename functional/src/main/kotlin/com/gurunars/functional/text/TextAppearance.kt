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

fun<T: WithTextAppearence> textAppearenceChangeSpec() = listOf<ChangeSpec<T, *, TextView>>(
    { it: T -> it.textAppearance.color } rendersTo { setTextColor(it.value) },
    { it: T -> it.textAppearance.gravity } rendersTo { gravity = it.value },
    { it: T -> it.textAppearance.size } rendersTo { textSize = context.toInt(it).toFloat() },
    { it: T -> it.textAppearance.textStyle } rendersTo {
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
)