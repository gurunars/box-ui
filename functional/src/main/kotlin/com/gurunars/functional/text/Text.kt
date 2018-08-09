package com.gurunars.functional.text

import android.content.Context
import android.widget.TextView
import com.gurunars.functional.*


data class Text(
    override val textAppearence: TextAppearence = TextAppearence(),
    val value: String = ""
): WithTextAppearence

class TextComponent : Component<Text> {
    private val changeSpec = listOf<ChangeSpec<Text, *, TextView>>(
        { it: Text -> it.value } rendersTo  { text = it }
    ) + textAppearenceChangeSpec()

    override val empty = Text()
    override fun getEmptyView(context: Context) = TextView(context).apply {
        setSingleLine(true)
    }
    override fun diff(old: Text, new: Text): List<Mutation> = changeSpec.diff(
        old,
        new
    )
}