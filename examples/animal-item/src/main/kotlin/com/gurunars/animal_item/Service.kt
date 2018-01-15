package com.gurunars.animal_item

import android.arch.persistence.room.Room
import android.content.Context
import com.gurunars.box.ui.DataSource
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class Service(private val db: Db) {

    val items = DataSource(
        db.animalItemDao::all,
        {
            persistOrderedList(
                db::runInTransaction,
                db.animalItemDao,
                it
            )
        },
        initial=listOf()
    )

    fun clear() {
        doAsync {
            try {
                db.animalItemDao.truncate()
                db.animalItemDao.resetCount()
            } catch (exe: Exception) {
                uiThread {
                    throw exe
                }
            }
        }
        items.set(listOf())
    }

    companion object {

        fun getRealService(ctx: Context) =
            Service(Room.databaseBuilder(
                ctx, Db::class.java, "AnimalList").build()
            )
    }
}
