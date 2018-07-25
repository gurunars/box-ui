package com.gurunars.functional.text

import android.graphics.Paint
import android.graphics.Typeface
import android.widget.TextView
import com.gurunars.functional.*

data class TextAppearence(
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
    val textAppearence: TextAppearence
}

fun<T: WithTextAppearence> textAppearenceChangeSpec() = listOf<ChangeSpec<T, *, TextView>>(
    { it: T -> it.textAppearence.color } rendersTo { setTextColor(it.value) },
    { it: T -> it.textAppearence.gravity } rendersTo { gravity = it.value },
    { it: T -> it.textAppearence.size } rendersTo { textSize = context.toInt(it).toFloat() },
    { it: T -> it.textAppearence.textStyle } rendersTo {
        when {
            it.isEmpty()
            -> setTypeface(typeface, Typeface.NORMAL)
            it.containsAll(setOf(TextAppearence.TextStyle.BOLD, TextAppearence.TextStyle.ITALIC))
            -> setTypeface(typeface, Typeface.BOLD_ITALIC)
            it.contains(TextAppearence.TextStyle.BOLD)
            -> setTypeface(typeface, Typeface.BOLD)
            it.contains(TextAppearence.TextStyle.ITALIC)
            -> setTypeface(typeface, Typeface.ITALIC)
        }

        paintFlags = when {
            it.contains(TextAppearence.TextStyle.UNDERLINE)
            -> paintFlags or Paint.UNDERLINE_TEXT_FLAG
            it.contains(TextAppearence.TextStyle.STRIKE_THROUGH)
            -> paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            else
            -> paintFlags
        }
    }
)