package com.gurunars.item_list

import java.io.Serializable

/**
 * Abstraction of the entity that can be shown in the ItemListView.
 */
abstract class Item: Serializable {

    /**
     * Item unique identifier
     */
    abstract fun getId(): Long

    /**
     * Item type
     */
    abstract fun getType(): Enum<*>

    /**
     * @param other another object to compare payload with
     * @return true if payloads are the same
     */
    abstract fun payloadsEqual(other: Item): Boolean

    final override fun equals(other: Any?) =
        other is Item &&
        getId() == other.getId() &&
        payloadsEqual(other)

}
