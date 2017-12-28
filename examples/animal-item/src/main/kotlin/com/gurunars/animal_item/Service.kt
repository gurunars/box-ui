package com.gurunars.animal_item

import android.app.Activity
import android.arch.persistence.room.Room
import com.gurunars.databinding.android.DataSource
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class Service(private val db: Db) {

    val items = DataSource(
        db.animalItemDao::all,
        {
            persistList(
                db::runInTransaction,
                db.animalItemDao,
                it.mapIndexed { index, animalItem ->
                    animalItem.copy(
                        id = Math.max(animalItem.id, 0),
                        position = index
                    )
                }
            )
        },
        { it.sortedBy { it.position } },
        listOf()
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

        fun Activity.getRealService() =
            Service(Room.databaseBuilder(
                this, Db::class.java, "AnimalList").build()
            )

    }

}

