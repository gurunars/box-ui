package com.gurunars.crud_item_list

import android.view.View
import com.gurunars.android_utils.Icon
import com.gurunars.box.IBox
import com.gurunars.box.IRoBox
import com.gurunars.box.box
import com.gurunars.item_list.Item
import com.gurunars.item_list.SelectableItem

/**
 * Aggregation of attributes describing a specific item type in the context of showing the item
 * list and editing individual items.
 *
 * @property icon Item icon for the creation menu.
 * @property type Type of the item the descriptor to be associated to.
 * @property title Value shown as a hint for the icon and as a title of the creation form
 */
interface ItemTypeDescriptor<ItemType : Item> {
    val icon: Icon
    val type: Enum<*>
        get() = Item.Default.ONLY

    /**
     * Return an observable of the title that is a specific for
     * each individual item
     */
    fun getItemTitle(item: IRoBox<ItemType>): IRoBox<String> =
        title.box

    val title: String

    /**
     * Represents validity status of form's payload.
     *
     * @property type severity of the status
     * @property message payload shown to the end user to describe the issue
     */
    data class Status(
        val type: Type,
        val message: String
    ) {
        /***/
        enum class Type(internal val isBlocking: Boolean) {
            /***/
            INFO(false),
            /***/
            WARNING(false),
            /***/
            ERROR(true)
        }

        companion object {
            /***/
            fun ok() = info("")
            /***/
            fun error(msg: String) = Status(Type.ERROR, msg)
            /***/
            fun warning(msg: String) = Status(Type.WARNING, msg)
            /***/
            fun info(msg: String) = Status(Type.INFO, msg)
        }
    }

    /**
     * Return a rendered row view for a specific item
     *
     * @param field - payload to be rendered
     */
    fun bindRow(field: IRoBox<SelectableItem<ItemType>>): View

    /** Returns status of the payload: OK, error, warning */
    fun validate(item: ItemType): Status

    /** Returns a newly created item of a specific type without saving it. */
    fun createNewItem(): ItemType

    /**
     * @param field - observable of the payload to be edited in the form
     * @return rendered form
     */
    fun bindForm(field: IBox<ItemType>): View
}
