package com.gurunars.item_list.selectable_example

import com.gurunars.item_list.Item

internal data class AnimalItem(private val id: Long, val version: Int, private val type: AnimalItem.Type) : Item {

    override fun getType(): Enum<*> {
        return type
    }

    override fun getId(): Long {
        return id
    }

    override fun payloadsEqual(other: Item): Boolean {
        return other is AnimalItem && version == other.version
    }

    internal enum class Type {
        MONKEY, TIGER, WOLF, LION, EMPTY
    }

    override fun toString(): String {
        return "#$id{$type @ $version}"
    }

}
