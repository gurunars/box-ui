package com.gurunars.animal_item

import com.gurunars.item_list.Item

data class AnimalItem(
    override val id: Long,
    override val type: AnimalItem.Type,
    val version: Int) : Item {

    enum class Type {
        MONKEY, TIGER, WOLF, LION, EMPTY
    }

    override fun toString(): String {
        return "#$id{$type @ $version}"
    }

}
