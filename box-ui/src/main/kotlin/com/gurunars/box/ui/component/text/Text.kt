package com.gurunars.box.ui.component.text

import android.content.Context
import android.graphics.Paint
import android.graphics.Typeface
import android.widget.TextView
import com.gurunars.box.ui.component.*


data class Text(
    //Controls whether links such as urls and email addresses are automatically found and converted to clickable links.
    //val autoLink: Boolean = false,
    val textAppearence: TextAppearence = TextAppearence(),
    val value: String = ""
)

class TextComponent : Component {
    private val changeSpec = listOf<ChangeSpec<Text, *, TextView>>(
        { it: Text -> it.value } rendersTo  { text = it },
        { it: Text -> it.textAppearence.color } rendersTo { setTextColor(it.value) },
        { it: Text -> it.textAppearence.gravity } rendersTo { gravity = it.value },
        { it: Text -> it.textAppearence.size } rendersTo { textSize = context.toInt(it).toFloat() },
        { it: Text -> it.textAppearence.textStyle } rendersTo {
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

    override val empty = Text()
    override fun getEmptyView(context: Context) = TextView(context).apply {
        setSingleLine(true)
    }
    override fun diff(old: Any, new: Any): List<Mutation> = changeSpec.diff(
        old as Text,
        new as Text
    )
}