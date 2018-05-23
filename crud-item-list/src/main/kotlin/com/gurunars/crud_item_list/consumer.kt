package com.gurunars.crud_item_list

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class Source<T>(private val consumer: (value: T) -> Unit) {

    fun from(supplier: () -> T) {
        Single.create<T> {
            supplier()
        }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { it -> consumer(it) }
    }

}

fun<T> consume(consumer: (value: T) -> Unit) =
    Source(consumer)

