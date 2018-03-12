package com.gurunars.animal_item

import android.app.Activity
import android.arch.persistence.room.Room
import com.gurunars.box.ui.DataSource
import com.gurunars.box.ui.bindToLifecycle
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class Service(
    private val activity: Activity,
    private val db: Db) {

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
    ).apply {
        bindToLifecycle(activity)
    }

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
            Service(
                this@getRealService,
                Room.databaseBuilder(
                    this@getRealService, Db::class.java, "AnimalList"
                ).build()
            )
    }
}
