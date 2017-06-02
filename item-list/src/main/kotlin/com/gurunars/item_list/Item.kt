package com.gurunars.item_list

import java.io.Serializable

/**
 * Abstraction of the entity that can be shown in the ItemListView.
 */
interface Item: Serializable {

    /**
     * Item unique identifier
     */
    fun getId(): Long

    /**
     * Item type
     */
    fun getType(): Enum<*>

    /**
     * @param other another object to compare payload with
     * @return true if payloads are the same
     */
    fun payloadsEqual(other: Item): Boolean

}
