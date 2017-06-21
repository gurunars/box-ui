package com.gurunars.crud_item_list.example

import com.gurunars.item_list.Item

internal class AnimalItem(private val id: Long, private var version: Int, private val type: AnimalItem.Type) : Item {

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

    fun update() {
        this.version++
    }

    fun getVersion() = version

    override fun toString(): String {
        return "#$id{$type @ $version}"
    }

}