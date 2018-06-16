package com.gurunars.box.ui.functional

import android.content.Context
import android.widget.TextView

data class Text(
    val value: String = "",
    val onClick: () -> Unit = {}
)

class TextComponent : Component {
    private val changeSpec = listOf<ChangeSpec<Text, *, TextView>>(
        { it: Text -> it.value } rendersTo  { text = it },
        { it: Text -> it.onClick } rendersTo  { this.setOnClickListener({ it() }) }
    )

    override val empty = Text()
    override fun getEmptyView(context: Context) = TextView(context)
    override fun diff(old: Any, new: Any): List<Mutation> = changeSpec.diff(
        old as Text,
        new as Text
    )
}