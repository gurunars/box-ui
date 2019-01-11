package com.gurunars.animal_item

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey

@Entity
data class AnimalItem @Ignore constructor(
    @PrimaryKey(autoGenerate = true)
    override var id: Long = 0,
    var type: AnimalItem.Type = Type.EMPTY,
    override var position: Int = 0,
    var version: Int = 0
) : ItemWithPosition {

    constructor(): this(0)

    enum class Type(val resourceId: Int) {
        MONKEY(R.drawable.ic_menu_monkey),
        TIGER(R.drawable.ic_menu_tiger),
        WOLF(R.drawable.ic_menu_wolf),
        LION(R.drawable.ic_menu_lion),
        EMPTY(R.drawable.ic_menu_empty)
    }

    override fun toString(): String =
        "#$id{$type @ $version}"
}
