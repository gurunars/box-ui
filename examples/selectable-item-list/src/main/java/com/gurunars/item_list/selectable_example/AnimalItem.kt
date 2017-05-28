package com.gurunars.item_list.selectable_example


import com.gurunars.item_list.Item

internal class AnimalItem constructor(id: Long, private var version: Int, type: AnimalItem.Type = AnimalItem.Type.MONKEY) : Item(id, type) {

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