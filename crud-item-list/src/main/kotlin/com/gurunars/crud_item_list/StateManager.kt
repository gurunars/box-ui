package com.gurunars.crud_item_list

import com.gurunars.databinding.BindableField
import com.gurunars.databinding.branch
import com.gurunars.databinding.field
import com.gurunars.databinding.patch
import com.gurunars.item_list.Item

internal enum class ViewMode(val hasOverlay: Boolean = true) {
    EMPTY,
    FORM,
    CONTEXTUAL(false),
    CREATION,
    LOADING
}

data class State<ItemType : Item>(
    val isCreationMode: Boolean = false,
    val explicitContextual: Boolean = false,
    val itemTypeInLoad: Enum<*>? = null,
    val selectedItems: Set<ItemType> = setOf(),
    val itemInEdit: ItemType? = null
) {

    val isOpen
        get() = viewMode != ViewMode.EMPTY

    internal val viewMode: ViewMode
        get() {
            if (itemInEdit != null) {
                return ViewMode.FORM
            } else if (itemTypeInLoad != null) {
                return ViewMode.LOADING
            } else if (explicitContextual || selectedItems.isNotEmpty()) {
                return ViewMode.CONTEXTUAL
            } else if (isCreationMode) {
                return ViewMode.CREATION
            } else {
                return ViewMode.EMPTY
            }
        }

}

internal class StateMachine<ItemType : Item>(
    private val itemTypes: Collection<Enum<*>>,
    private val loadItem: (itemType: Enum<*>) -> ItemType,
    private val asyncWrapper: (
        supplier: () -> ItemType,
        consumer: (item: ItemType) -> Unit
    ) -> Unit = { supplier, consumer ->
        0.asyncChain(supplier, consumer)
    }
) {

    val state = State<ItemType>().field

    val isOpen = false.field

    val itemInEdit: BindableField<ItemType?> =
        state.branch({ itemInEdit }, { copy(itemInEdit = it) })
    val selectedItems: BindableField<Set<ItemType>> =
        state.branch({ selectedItems }, { copy(selectedItems = it) })

    private fun openWithState(value: State<ItemType>) {
        if (state.get().viewMode == ViewMode.EMPTY) {
            state.set(value)
        }
    }

    fun openExplicitContextualMenu()
        = openWithState(State(explicitContextual = true))

    init {
        state.onChange { value ->
            isOpen.set(value.isOpen)
            if (value.itemTypeInLoad != null) {
                loadType(value.itemTypeInLoad)
            }
            if (value.viewMode == ViewMode.CREATION && itemTypes.size == 1) {
                loadType(itemTypes.first())
            }
        }
        isOpen.onChange { it ->
            if (it) {
                openWithState(State(isCreationMode = true))
            } else {
                state.set(State())
            }
        }
    }

    private fun loadType(itemType: Enum<*>) {
        asyncWrapper({
            loadItem(itemType)
        }, {
            state.patch {
                copy(
                    itemInEdit = it,
                    itemTypeInLoad = null
                )
            }
        })
    }

}