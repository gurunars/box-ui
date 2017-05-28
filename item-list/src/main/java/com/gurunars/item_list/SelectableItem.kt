package com.gurunars.item_list

/**
 * Wrapper around item item with an "isSelected" flag.
 *
 * @param <ItemType> type of the actual item.
 */
class SelectableItem<ItemType : Item> internal constructor(
        /**
         * @return actual item
         */
        val item: ItemType,
        /**
         * @return True if the item is selected
         */
        val isSelected: Boolean) : Item(item.id, item.type) {

    override fun payloadsEqual(obj: Item): Boolean {
        if (obj is SelectableItem<*>) {
            val other = obj
            return item.payloadsEqual(other.item) && isSelected == other.isSelected
        }
        return false
    }

    override fun toString(): String {
        return item.toString() + "|" + isSelected
    }
}
