package com.gurunars.crud_item_list

import android.graphics.Color
import android.view.View
import com.gurunars.android_utils.IconView
import com.gurunars.databinding.BindableField
import com.gurunars.item_list.Item
import com.gurunars.item_list.ItemViewBinder

/**
 * Aggregation of attributes describing a specific item type in the context of showing the item
 * list and editing individual items.
 *
 * @property icon Item icon for the creation menu.
 * @property type Type of the item the descriptor to be associated to.
 * @property rowRegularColor background color applied to a row when it is not selected
 * @property rowSelectionColor background color applied to a row when it is selected
 */
interface ItemTypeDescriptor<ItemType : Item> : ItemViewBinder<ItemType> {
    val icon: IconView.Icon
    val type: Enum<*>
        get() = Item.Default.ONLY
    val rowRegularColor: Int
        get() = Color.TRANSPARENT
    val rowSelectionColor: Int
        get() = Color.RED

    data class Status(
        val type: Type,
        val message: String
    ) {
        enum class Type(val isBlocking: Boolean) {
            INFO(false), WARNING(false), ERROR(true)
        }

        companion object {
            fun ok() = info("")
            fun error(msg: String) = Status(Type.ERROR, msg)
            fun warning(msg: String) = Status(Type.WARNING, msg)
            fun info(msg: String) = Status(Type.INFO, msg)
        }
    }

    /**
     * Return status of the payload: OK, error, warning
     */
    fun validate(item: ItemType): Status

    /**
     * Return a newly created item of a specific type without saving it.
     */
    fun createNewItem(): ItemType

    /**
     * @param field - observable of the payload to be edited in the form
     * @return rendered form
     */
    fun bindForm(field: BindableField<ItemType>): View
}
