package com.gurunars.box.ui.component

import android.content.Context
import android.widget.TextView

/**
 * fontFamily
 * gravity
 * letterSpacing
 */
data class Label(
    val text: String = "",
    val onClick: () -> Unit = {}
)

class TextComponent : Component {
    private val changeSpec = listOf<ChangeSpec<Label, *, TextView>>(
        { it: Label -> it.text } rendersTo  { text = it },
        { it: Label -> it.onClick } rendersTo  { this.setOnClickListener({ it() }) }
    )

    override val empty = Label()
    override fun getEmptyView(context: Context) = TextView(context).apply {
        setSingleLine(true)
    }
    override fun diff(old: Any, new: Any): List<Mutation> = changeSpec.diff(
        old as Label,
        new as Label
    )
}