package com.gurunars.box.ui.components.listcontroller

import com.gurunars.box.core.IReadOnlyObservableValue
import com.gurunars.box.core.bind
import com.gurunars.box.ui.components.listview.WithId

internal class ActionMoveUp<ItemType: WithId> : Action<ItemType> {

    override fun canPerform(
        state: IReadOnlyObservableValue<ListState<ItemType>>
    ): IReadOnlyObservableValue<Boolean> = state.bind {
        positions.let { isSolidChunk(it) && it.first() > 0 }
    }

    override suspend fun perform(
        state: ListState<ItemType>
    ): ListState<ItemType>  {
        val positions = state.positions
        val positionToMoveDown = positions[0] - 1
        val itemToMoveDown = state.all[positionToMoveDown]
        return ListState(
            mutableListOf<ItemType>().apply {
                addAll(state.all)
                add(positions[positions.size - 1] + 1, itemToMoveDown)
                removeAt(positionToMoveDown)
            },
            state.selected
        )
    }
}
