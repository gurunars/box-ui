package com.gurunars.item_list

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.TextView
import com.gurunars.box.IRoBox
import com.gurunars.box.oneWayBranch
import com.gurunars.box.ui.asRow
import com.gurunars.box.ui.text
import com.gurunars.box.ui.with


/**
 * @param box box representing item's payload
 * @return a view bound to a box holding the item
 */
typealias ItemViewBinder<ItemType> = (field: IRoBox<ItemType>) -> View

/***/
fun <ItemType : Item> Context.defaultItemViewBinder(field: IRoBox<ItemType>) = with<TextView> {
    asRow()
    setBackgroundColor(Color.YELLOW)
    setTextColor(Color.RED)
    text(field.oneWayBranch {
        toString().let {
            it.subSequence(0, minOf(42, it.length)).toString()
        }
    })
}
