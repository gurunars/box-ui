package com.gurunars.crud_item_list

import com.gurunars.item_list.Item

interface ClipboardSerializer<ItemType: Item> {
    fun fromString(count: Int, source: String): ItemType
    fun toString(source: ItemType): String
}

val ClipboardSerializer<*>.serializationLabel
    get() = "com.gurunars.crud_item_list:${this::class.java.canonicalName}"