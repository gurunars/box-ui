package com.gurunars.box.ui.components.listcontroller

import com.gurunars.box.core.IReadOnlyObservableValue
import com.gurunars.box.core.bind
import com.gurunars.box.ui.components.listview.WithId

internal class ActionDelete<ItemType: WithId> : Action<ItemType> {

    override fun canPerform(
        state: IReadOnlyObservableValue<ListState<ItemType>>
    ): IReadOnlyObservableValue<Boolean> = state.bind { selected.isNotEmpty() }

    override suspend fun perform(
        state: ListState<ItemType>
    ): ListState<ItemType> =
        ListState(
            state.all.filter { !state.selected.contains(it.id) },
            setOf()
        )
}
