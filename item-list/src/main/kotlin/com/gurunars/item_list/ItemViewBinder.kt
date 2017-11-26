package com.gurunars.item_list

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.TextView
import com.gurunars.databinding.Box
import com.gurunars.databinding.android.asRow
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.textColor
import com.gurunars.databinding.onChange

/**
 * @param field field representing item's payload
 * @return a view bound to a field holding the item
 */
typealias ItemViewBinder<ItemType> = (field: Box<ItemType>) -> View

fun <ItemType : Item> Context.defaultBindView(field: Box<ItemType>) = TextView(this).apply {
    backgroundColor = Color.YELLOW
    textColor = Color.RED
    field.onChange { value ->
        text = with(value.toString()) {
            substring(0, minOf(42, length))
        }
        asRow()
    }
}
