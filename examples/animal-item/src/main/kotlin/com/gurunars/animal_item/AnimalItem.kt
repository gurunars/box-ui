package com.gurunars.animal_item

import com.gurunars.item_list.Item

data class AnimalItem(
    override val id: Long,
    override val type: AnimalItem.Type,
    val version: Int) : Item() {

    override fun payloadsEqual(other: Item): Boolean {
        return other is AnimalItem && version == other.version
    }

    enum class Type {
        MONKEY, TIGER, WOLF, LION, EMPTY
    }

    override fun toString(): String {
        return "#$id{$type @ $version}"
    }

}
