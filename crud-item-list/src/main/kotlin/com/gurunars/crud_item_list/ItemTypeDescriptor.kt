package com.gurunars.crud_item_list

import android.view.View
import com.gurunars.android_utils.Icon
import com.gurunars.box.IBox
import com.gurunars.box.IRoBox
import com.gurunars.item_list.Item
import com.gurunars.item_list.SelectableItem

/**
 * Aggregation of attributes describing a specific item type in the context of showing the item
 * list and editing individual items.
 *
 * @property icon Item icon for the creation menu.
 * @property type Type of the item the descriptor to be associated to.
 */
interface ItemTypeDescriptor<ItemType : Item> {
    val icon: Icon
    val type: Enum<*>
        get() = Item.Default.ONLY

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
     * Return a rendered row view for a specific item
     *
     * @param field - payload to be rendered
     * @param triggerEdit - function that invokes item editing dialog for the item
     */
    fun bindRow(field: IRoBox<SelectableItem<ItemType>>, triggerEdit: () -> Unit = {}): View

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
    fun bindForm(field: IBox<ItemType>): View
}
