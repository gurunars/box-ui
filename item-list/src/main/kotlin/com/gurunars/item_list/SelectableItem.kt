package com.gurunars.item_list

import android.content.Context
import android.view.View
import com.gurunars.databinding.BindableField


interface SelectableItemViewBinder<ItemType: Item> {
    fun bind(context: Context, payload: BindableField<Pair<SelectableItem<ItemType>, SelectableItem<ItemType>?>>): View
    fun getEmptyPayload(): ItemType
}


/**
 * Wrapper around item item with an "isSelected" flag.
 *
 * @param <ItemType> type of the actual item.
 */
class SelectableItem<out ItemType : Item> internal constructor(val item: ItemType, val isSelected: Boolean) : Item {

    override fun getId() = item.getId()

    override fun getType() = item.getType()

    override fun payloadsEqual(other: Item) =
        other is SelectableItem<*> && item.payloadsEqual(other.item) && isSelected == other.isSelected

    override fun toString() =
        item.toString() + "|" + isSelected

    override fun equals(other: Any?) =
        other is SelectableItem<*> && other.getId() == getId()

    override fun hashCode() = getId().hashCode()
}
