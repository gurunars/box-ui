package com.gurunars.animal_item

import android.app.Activity
import android.arch.persistence.room.Room
import com.gurunars.item_list.persistence.DataSource
import com.gurunars.box.ui.bindToLifecycle
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class Service(
    private val activity: Activity,
    private val db: Db) {

    val items = DataSource(
        dao = db.animalItemDao,
        preprocess = {
            it.forEachIndexed { index, animalItem -> animalItem.position = index }
            it
        }
    ).apply {
        bindToLifecycle(activity)
    }

    fun clear() {
        doAsync {
            try {
                db.animalItemDao.truncate()
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
