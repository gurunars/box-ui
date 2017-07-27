package com.gurunars.item_list

import java.io.Serializable

/**
 * Abstraction of the entity that can be shown in the ItemListView.
 */
abstract class Item: Serializable {

    /**
     * Item unique identifier
     */
    abstract val id: Long

    /**
     * Item type
     */
    abstract val type: Enum<*>

    /**
     * @param other another object to compare payload with
     * @return true if payloads are the same
     */
    abstract fun payloadsEqual(other: Item): Boolean

    /**
     * @suppress
     */
    final override fun equals(other: Any?) =
        other is Item &&
        id == other.id &&
        payloadsEqual(other)

    /**
     * @suppress
     */
    final override fun hashCode() = id.hashCode()

}
