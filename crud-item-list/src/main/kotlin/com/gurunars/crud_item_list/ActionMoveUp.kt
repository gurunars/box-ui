package com.gurunars.crud_item_list

import com.gurunars.box.core.IRoBox
import com.gurunars.box.core.bind
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

internal class ActionMoveUp<ItemType : Item> : Action<ItemType> {

    override fun canPerform(
        state: IRoBox<ListState<ItemType>>
    ): IRoBox<Boolean> = state.bind {
        val positions = getPositions(all, selected)
        isSolidChunk(positions) && positions[0] > 0
    }

    override fun perform(
        state: ListState<ItemType>
    ): Single<ListState<ItemType>> = Single.fromCallable {
            val positions = getPositions(state.all, state.selected)
            val positionToMoveDown = positions[0] - 1
            val itemToMoveDown = state.all[positionToMoveDown]
            ListState(
                mutableListOf<ItemType>().apply {
                    addAll(state.all)
                    add(positions[positions.size - 1] + 1, itemToMoveDown)
                    removeAt(positionToMoveDown)
                },
                state.selected
            )
        }
        .subscribeOn(Schedulers.computation())
}
