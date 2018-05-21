package com.gurunars.crud_item_list

import com.gurunars.box.core.IRoBox
import com.gurunars.box.core.bind
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

internal class ActionDelete<ItemType : Item> : Action<ItemType> {

    override fun canPerform(
        state: IRoBox<ListState<ItemType>>
    ): IRoBox<Boolean> = state.bind { selected.isNotEmpty() }

    override fun perform(
        state: ListState<ItemType>
    ): Single<ListState<ItemType>> = Single.fromCallable {
        ListState(
            state.all.filter { !state.selected.contains(it.id) },
            setOf()
        )
    }
    .subscribeOn(Schedulers.computation())
}
