package com.gurunars.crud_item_list.example

import com.gurunars.item_list.Item

internal class AnimalItem @JvmOverloads constructor(id: Long, version: Int, type: AnimalItem.Type = AnimalItem.Type.MONKEY) : Item<AnimalItem.Type>(id, type) {

    var version: Int = 0
        private set

    override fun payloadsEqual(other: Item): Boolean {
        return other is AnimalItem && version == (other as AnimalItem).version
    }

    internal enum class Type {
        MONKEY, TIGER, WOLF, LION
    }

    fun update() {
        this.version++
    }

    init {
        this.version = version
    }

    constructor(id: Long, type: Type) : this(id, 0, type) {}

    constructor(type: Type) : this(0, 0, type) {}

    override fun toString(): String {
        return "#" + getId() + "{" + getType() + " @ " + version + "}"
    }

}
