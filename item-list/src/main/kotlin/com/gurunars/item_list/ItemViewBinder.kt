package com.gurunars.item_list

import android.content.Context
import android.graphics.Color
import android.widget.TextView
import com.gurunars.databinding.BindableField
import com.gurunars.databinding.android.Component
import com.gurunars.databinding.android.asRow
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.textColor

typealias ItemViewBinder<ItemType> = (field: BindableField<ItemType>) -> Component

/**
 * @param field field representing item's payload
 */
class DefaultBindView<ItemType>(private val field: BindableField<ItemType>) : Component {
    override fun Context.render() = TextView(this).apply {
        backgroundColor = Color.YELLOW
        textColor = Color.RED
        field.onChange {
            text = with(field.get().toString()) {
                substring(0, minOf(42, length))
            }
            asRow()
        }
    }
}
