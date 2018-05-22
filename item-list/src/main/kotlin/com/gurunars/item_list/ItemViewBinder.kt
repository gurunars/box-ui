package com.gurunars.item_list

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.TextView
import com.gurunars.box.IRoBox
import com.gurunars.box.ui.asRow

/**
 * @param box box representing item's payload
 * @return a view bound to a box holding the item
 */
typealias ItemViewBinder<ItemType> = (field: IRoBox<ItemType>) -> View

/***/
fun <ItemType : Item> Context.defaultItemViewBinder(field: IRoBox<ItemType>) = TextView(this).apply {
    setBackgroundColor(Color.YELLOW)
    setTextColor(Color.RED)
    field.onChange { value ->
        text = with(value.toString()) {
            substring(0, minOf(42, length))
        }
        asRow()
    }
}
