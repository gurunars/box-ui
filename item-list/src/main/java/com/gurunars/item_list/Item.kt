package com.gurunars.item_list

import java.io.Serializable

/**
 * Abstraction of the entity that can be shown in the ItemListView.

 * "equals" method MUST be implemented to compare items subclasses by value.
 */
abstract class Item protected constructor(
        /**
         * @return a unique ID of a persisted item
         */
        val id: Long,
        /**
         * @return item type
         */
        val type: Enum<*>) : Serializable {

    /**
     * @param other another object to compare payload with
     * *
     * @return true if payloads are the same
     */
    abstract fun payloadsEqual(other: Item): Boolean

    override fun equals(other: Any?): Boolean {
        return other != null &&
                this.javaClass == other.javaClass &&
                id == (other as Item).id
    }

    override fun hashCode(): Int {
        return java.lang.Long.valueOf(id)!!.hashCode()
    }

}
