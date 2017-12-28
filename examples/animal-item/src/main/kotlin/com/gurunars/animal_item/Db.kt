package com.gurunars.animal_item

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context

@Database(
    entities = arrayOf(AnimalItem::class),
    version = 1
)
@TypeConverters(Converters::class)
abstract class Db : RoomDatabase() {
    abstract val animalItemDao: AnimalItemDao

    companion object {
        fun Context.initDb() =
            Room.databaseBuilder(this, Db::class.java, "AnimalItem").build()
    }
}
