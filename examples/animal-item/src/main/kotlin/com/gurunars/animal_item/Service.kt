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
        initial = listOf(),
        /*
        NOTE: small timeout for debounce is the source of the UI glitches
        But in this sample it is a necessary evil to make the tests work fast.
        */
        timeout = 100
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
