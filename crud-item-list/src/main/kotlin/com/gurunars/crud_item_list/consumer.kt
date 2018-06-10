package com.gurunars.crud_item_list

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class Source<T>(private val supplier: () -> T) {

    fun to(consumer: (value: T) -> Unit) {
        Single.create<T> { supplier() }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { it -> consumer(it) }
    }

}


fun<T> from(supplier: () -> T) =
    Source(supplier)

