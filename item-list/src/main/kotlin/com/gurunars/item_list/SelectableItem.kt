package com.gurunars.item_list

/**
 * Wrapper around item item with an "isSelected" flag.
 *
 * @param <ItemType> type of the actual item.
 */
class SelectableItem<ItemType : Item> internal constructor(val item: ItemType, val isSelected: Boolean) : Item {

    override fun getId(): Long {
        return item.getId()
    }

    override fun getType(): Enum<*> {
        return item.getType()
    }

    override fun payloadsEqual(other: Item): Boolean {
        return other is SelectableItem<*> && item.payloadsEqual(other.item) && isSelected == other.isSelected
    }

    override fun toString(): String {
        return item.toString() + "|" + isSelected
    }

    override fun equals(other: Any?): Boolean {
        return other is SelectableItem<*> && other.getId() == getId()
    }

    override fun hashCode(): Int {
        return getId().hashCode()
    }
}
