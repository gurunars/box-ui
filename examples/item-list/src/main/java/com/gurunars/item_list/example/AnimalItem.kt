package com.gurunars.item_list.example

import com.gurunars.item_list.Item

internal class AnimalItem(id: Long, private var version: Int, type: AnimalItem.Type) : Item(id, type) {

    override fun payloadsEqual(other: Item): Boolean {
        return other is AnimalItem && version == other.version
    }

    internal enum class Type {
        MONKEY, TIGER, WOLF, LION
    }

    fun update() {
        this.version++
    }

    override fun toString(): String {
        return "#$id{$type @ $version}"
    }

}
