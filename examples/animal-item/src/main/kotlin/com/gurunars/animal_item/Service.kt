package com.gurunars.animal_item

import android.app.Activity
import android.arch.persistence.room.Room
import com.gurunars.item_list.persistence.DataSource
import com.gurunars.box.ui.bindToLifecycle
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

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

    fun clear() =
        Completable
            .fromCallable { db.animalItemDao.truncate() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete { items.set(listOf()) }
            .doOnError { throw it }

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
