package com.gurunars.box.ui.components.listcontroller

import com.gurunars.box.core.IReadOnlyObservableValue
import com.gurunars.box.core.bind
import com.gurunars.box.ui.components.listview.WithId

internal class ActionMoveDown<ItemType: WithId> : Action<ItemType> {

    override fun canPerform(
        state: IReadOnlyObservableValue<ListState<ItemType>>
    ): IReadOnlyObservableValue<Boolean> = state.bind {
        positions.let { isSolidChunk(it) && it.last() < all.size - 1 }
    }

    override suspend fun perform(
        state: ListState<ItemType>
    ): ListState<ItemType> {
        val positions = state.positions
        val positionToMoveUp = positions[positions.size - 1] + 1
        val itemToMoveUp = state.all[positionToMoveUp]
        return ListState(
            mutableListOf<ItemType>().apply {
                addAll(state.all)
                removeAt(positionToMoveUp)
                add(positions[0], itemToMoveUp)
            },
            state.selected
        )
    }

}
