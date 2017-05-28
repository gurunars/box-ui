package com.gurunars.item_list


internal class AnimalItem @JvmOverloads constructor(id: Long, private var version: Int, type: AnimalItem.Type = AnimalItem.Type.MONKEY) : Item(id, type) {

    override fun payloadsEqual(other: Item): Boolean {
        return other is AnimalItem && version == (other as AnimalItem).version
    }

    internal enum class Type {
        MONKEY, TIGER, WOLF, LION
    }

    fun update() {
        this.version++
    }

    constructor(id: Long, type: Type) : this(id, 0, type) {}

    override fun toString(): String {
        return "#$id{$type @ $version}"
    }

}
