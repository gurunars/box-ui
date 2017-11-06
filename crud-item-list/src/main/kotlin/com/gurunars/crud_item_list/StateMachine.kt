package com.gurunars.crud_item_list

import com.gurunars.databinding.BindableField
import com.gurunars.item_list.Item

internal enum class Overlay {
    SAME,
    YES,
    NO
}

internal enum class ViewMode(val overlay: Overlay = Overlay.YES) {
    EMPTY(Overlay.SAME),
    FORM,
    CONTEXTUAL(Overlay.NO),
    CREATION,
    LOADING
}

internal class StateMachine<ItemType : Item>(
    private val selectedItems: BindableField<Set<ItemType>>,
    private val isOpen: BindableField<Boolean>,
    private val itemInEdit: BindableField<ItemType?>,
    private val viewMode: BindableField<ViewMode>,
    //
    private val itemTypes: BindableField<Collection<Enum<*>>>,
    private val loadItem: (itemType: Enum<*>) -> ItemType,
    private val bindForm: (item: ItemType) -> Unit,
    private val asyncWrapper: (
        supplier: () -> ItemType,
        consumer: (item: ItemType) -> Unit
    ) -> Unit = { supplier, consumer ->
        0.asyncChain(supplier, consumer)
    }
) {

    init {
        isOpen.onChange {
            if (!it) {
                close()
            } else {
                startCreation()
            }
        }
        selectedItems.onChange {
            if (it.isEmpty()) {
                close()
            } else {
                startSelection()
            }
        }
        itemInEdit.onChange {
            if (it == null) {
                close()
            } else {
                openForm(it)
            }
        }
    }

    private fun openForm(item: ItemType) {
        bindForm(item)
        viewMode.set(ViewMode.FORM)
        isOpen.set(true)
    }

    private fun startCreation() {
        if (viewMode.get() != ViewMode.EMPTY) return
        isOpen.set(true)
        val types = itemTypes.get()
        if (types.size == 1) {
            viewMode.set(ViewMode.LOADING)
            asyncWrapper(
                { loadItem(types.first()) },
                { itemInEdit.set(it) }
            )
        } else {
            viewMode.set(ViewMode.CREATION)
        }
    }

    private fun startSelection() {
        viewMode.set(ViewMode.CONTEXTUAL)
        isOpen.set(true)
        val item = itemInEdit.get()
        if (item != null) {
            openForm(item)
        }
    }

    private fun close() {
        viewMode.set(ViewMode.EMPTY)
        isOpen.set(false)
        selectedItems.set(setOf())
        itemInEdit.set(null)
    }

    fun clear() = close()

}