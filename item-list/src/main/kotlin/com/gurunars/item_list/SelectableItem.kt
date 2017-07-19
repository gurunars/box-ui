package com.gurunars.item_list


/**
 * Wrapper around item item with an "isSelected" flag.
 *
 * @param <ItemType> type of the actual item
 * @param item the actual item
 * @param isSelected a flag indicating if this particular item should be marked as selected
 */
class SelectableItem<out ItemType : Item> internal constructor(
    val item: ItemType,
    val isSelected: Boolean
) : Item() {

    override fun getId() = item.getId()

    override fun getType() = item.getType()

    override fun payloadsEqual(other: Item) =
        other is SelectableItem<*> &&
        item.payloadsEqual(other.item) &&
        isSelected == other.isSelected

    override fun toString() =
        item.toString() + "|" + isSelected

    override fun hashCode() = getId().hashCode()
}
