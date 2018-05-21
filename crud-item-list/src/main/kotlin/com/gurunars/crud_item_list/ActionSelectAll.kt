package com.gurunars.crud_item_list

import com.gurunars.box.core.IRoBox
import com.gurunars.box.core.bind
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

internal class ActionSelectAll<ItemType : Item> : Action<ItemType> {

    override fun canPerform(
        state: IRoBox<ListState<ItemType>>
    ): IRoBox<Boolean> = state.bind { selected.size < all.size }

    override fun perform(
        state: ListState<ItemType>
    ): Single<ListState<ItemType>> = Single.fromCallable {
            ListState(
                state.all,
                state.all.map { it.id }.toSet()
            )
        }
        .subscribeOn(Schedulers.computation())

}
