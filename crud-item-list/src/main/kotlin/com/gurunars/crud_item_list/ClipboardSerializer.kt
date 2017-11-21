package com.gurunars.crud_item_list

import com.gurunars.item_list.Item

/**
 * A mean to serialize an item as a string to transfer it via clipboard.
 */
interface ClipboardSerializer<ItemType: Item> {
    fun fromString(source: String): List<ItemType>
    fun toString(source: List<ItemType>): String
}

// TODO: it should be possible to paste via a pure textual serializer
internal val ClipboardSerializer<*>.serializationLabel
    get() = "com.gurunars.crud_item_list:${this::class.java.canonicalName}"