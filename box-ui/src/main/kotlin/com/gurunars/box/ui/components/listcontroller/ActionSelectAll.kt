package com.gurunars.box.ui.components.listcontroller

import com.gurunars.box.core.IReadOnlyObservableValue
import com.gurunars.box.core.bind
import com.gurunars.box.ui.components.listview.WithId

internal class ActionSelectAll<ItemType: WithId> : Action<ItemType> {

    override fun canPerform(
        state: IReadOnlyObservableValue<ListState<ItemType>>
    ): IReadOnlyObservableValue<Boolean> = state.bind { selected.size < all.size }

    override suspend fun perform(
        state: ListState<ItemType>
    ): ListState<ItemType> =
        ListState(
            state.all,
            state.all.map { it.id }.toSet()
        )
}
