package com.gurunars.animal_item

import android.arch.persistence.room.TypeConverter

class Converters {

    @TypeConverter
    fun fromType(value: AnimalItem.Type): Int =
        value.ordinal

    @TypeConverter
    fun toType(value: Int): AnimalItem.Type =
        AnimalItem.Type.values()[value]
}