package com.gurunars.item_list


/**
 * Wrapper around item item with an "isSelected" flag.
 *
 * @param ItemType type of the actual item
 * @property item the actual item
 * @property isSelected a flag indicating if this particular item should be marked as selected
 */
class SelectableItem<out ItemType : Item> internal constructor(
    val item: ItemType,
    val isSelected: Boolean
) : Item() {

    /**
     * @see Item.getId
     */
    override fun getId() = item.getId()

    /**
     * @see Item.getType
     */
    override fun getType() = item.getType()

    /**
     * @see Item.payloadsEqual
     */
    override fun payloadsEqual(other: Item) =
        other is SelectableItem<*> &&
        item.payloadsEqual(other.item) &&
        isSelected == other.isSelected

    /**
     * @suppress
     */
    override fun toString() =
        item.toString() + "|" + isSelected

}
