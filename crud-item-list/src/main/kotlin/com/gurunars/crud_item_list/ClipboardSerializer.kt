package com.gurunars.crud_item_list

import com.gurunars.item_list.Item

/** A mean to serialize an item as a string to transfer it via clipboard. */
interface ClipboardSerializer<ItemType : Item> {
    /** Obtains a collection of items from a string text in clipboard. */
    fun fromString(source: String): List<ItemType>
    /** Stores a collection of items as a string text in clipboard. */
    fun toString(source: List<ItemType>): String
}

// TODO: it should be possible to paste via a pure textual serializer
internal val ClipboardSerializer<*>.serializationLabel
    get() = "com.gurunars.crud_item_list:${this::class.java.canonicalName}"