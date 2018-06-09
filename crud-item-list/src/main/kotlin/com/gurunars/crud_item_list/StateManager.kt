package com.gurunars.crud_item_list

import com.gurunars.box.IBox
import com.gurunars.box.box
import com.gurunars.box.branch
import com.gurunars.box.patch
import com.gurunars.item_list.Item
import java.io.Serializable

internal enum class ViewMode(val hasOverlay: Boolean = true) {
    EMPTY,
    FORM,
    CONTEXTUAL(false),
    CREATION,
    LOADING
}

internal data class State<out ItemType : Item>(
    val isCreationMode: Boolean = false,
    val explicitContextual: Boolean = false,
    val itemTypeInLoad: Enum<*>? = null,
    val selectedItems: Set<ItemType> = setOf(),
    val itemInEdit: ItemType? = null
) : Serializable {

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
    ) -> Unit = { supplier, consumer -> from(supplier).to(consumer) }
) {

    val state = State<ItemType>().box

    private var previousState: State<ItemType> = State()

    val isOpen = false.box

    val viewMode = ViewMode.EMPTY.box

    val selectedItems: IBox<Set<ItemType>> =
        state.branch({ selectedItems }, { copy(selectedItems = it) })

    private fun openWithState(value: State<ItemType>) {
        if (state.get().viewMode == ViewMode.EMPTY) {
            state.set(value)
        }
    }

    init {
        state.onChange { value ->
            if (value.itemTypeInLoad != null) {
                asyncWrapper(
                    { itemTypes[value.itemTypeInLoad]!!.prepareNewItem() },
                    { this.loadItem(it) }
                )
            }
            if (previousState.itemInEdit == null && value.itemInEdit != null) {
                openForm(value.itemInEdit)
            }
            // View mode changes are allowed only when the menu is open to prevent
            // ugly screen changes
            if (value.isOpen) {
                viewMode.set(value.viewMode)
            }
            isOpen.set(value.isOpen)
            previousState = value
        }
        isOpen.onChange { it ->
            if (it) {
                if (itemTypes.size == 1) {
                    openWithState(State(itemTypeInLoad = itemTypes.keys.first()))
                } else {
                    openWithState(State(isCreationMode = true))
                }
            } else {
                state.set(State())
            }
        }
    }

    fun loadItem(item: ItemType?) =
        state.patch { State(itemInEdit = item) }

    fun loadType(itemType: Enum<*>) =
        state.patch { State(itemTypeInLoad = itemType) }

    fun openExplicitContextualMenu() =
        openWithState(State(explicitContextual = true))
}