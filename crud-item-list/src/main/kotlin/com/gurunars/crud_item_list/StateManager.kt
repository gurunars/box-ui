package com.gurunars.crud_item_list

import android.util.Log
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

data class State<out ItemType : Item>(
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
            return if (itemInEdit != null) {
                ViewMode.FORM
            } else if (itemTypeInLoad != null) {
                ViewMode.LOADING
            } else if (explicitContextual || selectedItems.isNotEmpty()) {
                ViewMode.CONTEXTUAL
            } else if (isCreationMode) {
                ViewMode.CREATION
            } else {
                ViewMode.EMPTY
            }
        }

}

internal class StateMachine<ItemType : Item>(
    private val openForm: (item: ItemType) -> Unit,
    private val itemTypes: Map<Enum<*>, ItemTypeDescriptor<ItemType>>,
    private val asyncWrapper: (
        supplier: () -> ItemType,
        consumer: (item: ItemType) -> Unit
    ) -> Unit = { supplier, consumer ->
        0.asyncChain(supplier, consumer)
    }
) {

    val state = State<ItemType>().field

    val isOpen = false.field

    val viewMode = ViewMode.EMPTY.field

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
                loadType(itemTypes.keys.first())
            }
            if (value.itemInEdit != null) {
                openForm(value.itemInEdit)
            }
            // View mode changes are allowed only when the menu is open to prevent
            // ugly screen changes
            if (value.isOpen) {
                viewMode.set(value.viewMode)
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

    fun loadItem(item: ItemType)
        = state.patch {
            copy(
                itemInEdit = item,
                itemTypeInLoad = null
            )
        }

    fun loadType(itemType: Enum<*>)
        = asyncWrapper({ itemTypes[itemType]!!.createNewItem() }, this::loadItem)

}