package com.gurunars.crud_item_list

import com.gurunars.box.core.IRoBox
import com.gurunars.box.core.bind
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

internal class ActionMoveDown<ItemType : Item> : Action<ItemType> {

    override fun canPerform(
        state: IRoBox<ListState<ItemType>>
    ): IRoBox<Boolean> = state.bind {
        val positions = getPositions(all, selected)
        isSolidChunk(positions) && positions[positions.size - 1] < all.size - 1
    }

    override fun perform(
        state: ListState<ItemType>
    ): Single<ListState<ItemType>> = Single.fromCallable {
            val positions = getPositions(state.all, state.selected)
            val positionToMoveUp = positions[positions.size - 1] + 1
            val itemToMoveUp = state.all[positionToMoveUp]
            ListState(
                mutableListOf<ItemType>().apply {
                    addAll(state.all)
                    removeAt(positionToMoveUp)
                    add(positions[0], itemToMoveUp)
                },
                state.selected
            )
        }
        .subscribeOn(Schedulers.computation())

}
