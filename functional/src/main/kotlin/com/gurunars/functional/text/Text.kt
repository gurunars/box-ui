package com.gurunars.functional.text

import android.content.Context
import android.widget.TextView
import com.gurunars.functional.*


data class Text(
    override val textAppearance: TextAppearance = TextAppearance(),
    val value: String = ""
): WithTextAppearence

class TextBinder : ElementBinder {
    private val changeSpec = changeSpecs<Text, TextView> {
        rendersTo({ value })  { text = it }
    } + textAppearanceChangeSpec()

    override val empty = Text()
    override fun getEmptyTarget(context: Context) = TextView(context).apply {
        setSingleLine(true)
    }
    override fun diff(context: Context, old: Any, new: Any): List<Mutation> = changeSpec.diff(
        old as Text,
        new as Text
    )
}