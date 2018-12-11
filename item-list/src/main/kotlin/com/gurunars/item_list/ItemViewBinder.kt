package com.gurunars.item_list

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.gurunars.box.IRoBox
import com.gurunars.box.ui.asRow
import com.gurunars.box.ui.fullSize
import kotlin.reflect.KClass

/**
 * @param field box representing item's payload
 * @return a view bound to a box holding the item
 */
typealias RenderItem<ItemType> = (field: IRoBox<ItemType>) -> View

/***/
fun Context.defaultItemViewBinder(field: IRoBox<Item>) = TextView(this).apply {
    setBackgroundColor(Color.YELLOW)
    setTextColor(Color.RED)
    field.onChange { value ->
        text = with(value.toString()) {
            substring(0, minOf(42, length))
        }
        asRow()
    }
}

class ItemBinding<ItemType: Item>(
    val type: KClass<ItemType>,
    private val render: RenderItem<ItemType>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ItemBinding<*>) return false
        if (type != other.type) return false
        return true
    }

    override fun hashCode(): Int = type.hashCode()

    @Suppress("UNCHECKED_CAST")
    internal fun renderGeneric(item: IRoBox<Item>) =
        this.render(item as IRoBox<ItemType>)
}

infix fun <ItemType: Item> KClass<ItemType>.rendersTo(render: RenderItem<ItemType>): ItemBinding<ItemType> =
    ItemBinding(this, render)

/** View binder for the case when there are no item in the list. */
typealias EmptyViewBinder = () -> View

/***/
fun Context.defaultEmptyViewBinder() = TextView(this).apply {
    id = R.id.noItemsLabel
    fullSize()
    text = getString(R.string.empty)
    gravity = Gravity.CENTER
}
