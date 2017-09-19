package com.gurunars.item_list

import java.io.Serializable

/**
 * Abstraction of the entity that can be shown in the ItemListView.
 */
interface Item: Serializable {

    /**
     * Default type for the cases when the list contains a set of homogeneous items.
     */
    enum class Default { ONLY }

    /**
     * Item unique identifier.
     */
    val id: Long

    /**
     * Item type.
     */
    val type: Enum<*>
        get() = Default.ONLY

}
