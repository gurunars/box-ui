package com.gurunars.item_list

/**
 * Wrapper around item item with an "isSelected" flag.
 *
 * @param <ItemType> type of the actual item.
 */
class SelectableItem<ItemType : Item> internal constructor(val item: ItemType, val isSelected: Boolean) : Item {

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
