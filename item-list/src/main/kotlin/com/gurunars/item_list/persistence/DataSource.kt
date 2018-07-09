package com.gurunars.item_list.persistence

import com.gurunars.box.*
import com.gurunars.item_list.Item
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

fun <ItemType : Item> persistList(
    dao: PlainDao<ItemType>,
    items: List<ItemType>
) = with(dao) {
    val source = dao.all()
    val sourceIds = source.map { it.id }.toSet()
    val targetIds = items.map { it.id }.toSet()
    val idsToRemove = sourceIds - targetIds
    delete(items.filter { idsToRemove.contains(it.id) })
    update(items.filter { sourceIds.contains(it.id) })
    insert(items.filterNot { sourceIds.contains(it.id) })
}

class DataSource<ItemType : Item> private constructor(
    private val dao: PlainDao<ItemType>,
    private val box: BoxWithLifecycle<List<ItemType>>
) : IBox<List<ItemType>> by box, WithLifecycle by box {

    constructor(
        dao: PlainDao<ItemType>,
        preprocess: (value: List<ItemType>) -> List<ItemType> = { it }
    ) : this(
        dao,
        Box(listOf<ItemType>()).withPreprocessor(preprocess).withLifecycle
    )

    private val _ready = Box(true)
    val ready = _ready

    init {
        reload()
        box.toObservable()
            .skip(1)  // skip the initial text
            .observeOn(Schedulers.io())
            .subscribe { persistList(dao, it) }
    }

    fun reload() {
        Single
            .fromCallable { _ready.set(false) }
            .subscribeOn(AndroidSchedulers.mainThread())
            .observeOn(Schedulers.io())
            .map { dao.all() }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { it ->
                box.set(it)
                _ready.set(true)
            }
    }

}