package com.gurunars.item_list

import java.io.Serializable

/** Abstraction of the entity that can be shown in the ItemListView. */
interface Item : Serializable {
    /** Item unique identifier. */
    val id: Long
}
