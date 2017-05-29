package com.gurunars.item_list

internal data class AnimalItem(private val id: Long, private val version: Int, private val type: AnimalItem.Type) : Item {

    constructor(id: Long, type: AnimalItem.Type) : this(id,0,type)

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
        MONKEY, TIGER, WOLF, LION
    }

    override fun toString(): String {
        return "#$id{$type @ $version}"
    }

}