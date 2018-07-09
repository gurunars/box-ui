package com.gurunars.box.ui.component.text

import android.content.Context
import android.widget.TextView
import com.gurunars.box.ui.component.*


data class Text(
    override val textAppearence: TextAppearence = TextAppearence(),
    val value: String = ""
): WithTextAppearence

class TextComponent : Component {
    private val changeSpec = listOf<ChangeSpec<Text, *, TextView>>(
        { it: Text -> it.value } rendersTo  { text = it }
    ) + textAppearenceChangeSpec()

    override val empty = Text()
    override fun getEmptyView(context: Context) = TextView(context).apply {
        setSingleLine(true)
    }
    override fun diff(old: Any, new: Any): List<Mutation> = changeSpec.diff(
        old as Text,
        new as Text
    )
}