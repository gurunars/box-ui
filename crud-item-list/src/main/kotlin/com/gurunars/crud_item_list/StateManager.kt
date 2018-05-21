package com.gurunars.crud_item_list

import android.util.Log
import com.gurunars.box.Box
import com.gurunars.box.core.IBox
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.io.Serializable

internal enum class ViewMode(val hasOverlay: Boolean = true) {
    EMPTY,
    FORM,
    CONTEXTUAL(false),
    CREATION,
    LOADING
}

internal data class State<ItemType : Any>(
    val isCreationMode: Boolean = false,
    val explicitContextual: Boolean = false,
    val itemTypeInLoad: Any? = null,
    val selectedItems: Set<Long> = setOf(),
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
    private val itemTypes: Map<Any, ItemTypeDescriptor<ItemType>>

) {

    val state = Box(State<ItemType>())

    private var previousState: State<ItemType> = State()

    val isOpen = Box(false)

    val viewMode = Box(ViewMode.EMPTY)

    val selectedItems: IBox<Set<Long>> =
        state.bind({ selectedItems }, { copy(selectedItems = it) })

    private fun openWithState(value: State<ItemType>) {
        if (state.get().viewMode == ViewMode.EMPTY) {
            state.set(value)
        }
    }

    init {
        state.onChange { value ->
            if (value.itemTypeInLoad != null) {
                Single.fromCallable {
                    itemTypes[value.itemTypeInLoad]!!.createNewItem()
                }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { item -> loadItem(item) }
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

    fun loadType(itemType: Any) =
        state.patch { State(itemTypeInLoad = itemType) }

    fun openExplicitContextualMenu() =
        openWithState(State(explicitContextual = true))
}