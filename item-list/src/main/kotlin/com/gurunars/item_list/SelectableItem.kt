package com.gurunars.item_list

/**
 * Wrapper around item item with an "isSelected" flag.
 *
 * @param ItemType type of the actual item
 * @property item the actual item
 * @property isSelected a flag indicating if this particular item should be marked as selected
 */
data class SelectableItem<out ItemType : Item> internal constructor(
    val item: ItemType,
    val isSelected: Boolean
) : Item {

    /**
     * @see Item.id
     */
    override val id = item.id

    /**
     * @see Item.type
     */
    override val type = item.type

    /**
     * @suppress
     */
    override fun toString() =
        item.toString() + "|" + isSelected
}
