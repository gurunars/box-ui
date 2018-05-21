package com.gurunars.crud_item_list

import com.gurunars.box.core.IRoBox
import com.gurunars.box.core.bind
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

internal class ActionEdit<ItemType : Item>(
    private val openForEdit: (item: ItemType) -> Unit
) : Action<ItemType> {

    override fun canPerform(
        state: IRoBox<ListState<ItemType>>
    ): IRoBox<Boolean> = state.bind { selected.size == 1 }

    override fun perform(
        state: ListState<ItemType>
    ): Single<ListState<ItemType>> = Single.fromCallable { state.all.first { state.selected.contains(it.id) } }
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .doOnSuccess { openForEdit(it) }
        .map { state }

}