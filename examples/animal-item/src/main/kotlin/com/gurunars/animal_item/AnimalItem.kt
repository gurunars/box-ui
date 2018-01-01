package com.gurunars.animal_item

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.gurunars.item_list.Item

@Entity
data class AnimalItem(
    @PrimaryKey(autoGenerate = true)
    override var id: Long = 0,
    override var type: AnimalItem.Type = Type.EMPTY,
    override var position: Int = 0,
    var version: Int = 0
) : ItemWithPosition {

    enum class Type {
        MONKEY, TIGER, WOLF, LION, EMPTY
    }

    override fun toString(): String {
        return "#$id{$type @ $version}"
    }
}
