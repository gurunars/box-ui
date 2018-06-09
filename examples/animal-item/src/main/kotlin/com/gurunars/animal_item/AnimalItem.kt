package com.gurunars.animal_item

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import com.gurunars.item_list.Item

@Entity
data class AnimalItem @Ignore constructor(
    @PrimaryKey
    override var id: Long = 0,
    override var type: AnimalItem.Type = Type.EMPTY,
    var version: Int = 0
) : Item {

    var position: Int = 0

    constructor(): this(0)

    enum class Type(val resourceId: Int) {
        MONKEY(R.drawable.ic_menu_monkey), TIGER(R.drawable.ic_menu_tiger),
        WOLF(R.drawable.ic_menu_wolf), LION(R.drawable.ic_menu_lion),
        EMPTY(R.drawable.ic_menu_empty)
    }

    override fun toString(): String {
        return "#$id{$type @ $version}"
    }
}
