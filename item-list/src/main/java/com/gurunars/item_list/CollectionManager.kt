package com.gurunars.item_list

import com.esotericsoftware.kryo.Kryo

import org.objenesis.strategy.StdInstantiatorStrategy

import java.io.Serializable
import java.util.ArrayList
import java.util.HashSet


internal class CollectionManager<ItemType : Item>(
        private val stateChangeHandler: (items: List<SelectableItem<ItemType>>) -> Unit,
        private val selectionChangeListener: Runnable) : Serializable {

    private val kryo = Kryo()

    private var items: List<ItemType> = ArrayList()
    private var selectedItems: Set<ItemType> = HashSet()

    init {
        this.kryo.instantiatorStrategy = Kryo.DefaultInstantiatorStrategy(StdInstantiatorStrategy())
    }

    private fun changed(newItems: List<ItemType>, newSelection: Set<ItemType>) {
        this.items = newItems
        val filteredSelection = newSelection.filter { items.contains(it) }.map { items[items.indexOf(it)] }.toHashSet()
        val selectionChanged = selectedItems != filteredSelection
        selectedItems = filteredSelection
        stateChangeHandler(items.map { SelectableItem(it, selectedItems.contains(it)) })
        if (selectionChanged) {
            selectionChangeListener.run()
        }
    }

    fun itemClick(selectableItem: SelectableItem<ItemType>) {
        if (selectedItems.isEmpty()) {
            return
        }

        val item = selectableItem.item

        val newSelectedItems = HashSet(selectedItems)

        if (newSelectedItems.contains(item)) {
            newSelectedItems.remove(item)
        } else {
            newSelectedItems.add(item)
        }
        changed(items, newSelectedItems)
    }

    fun itemLongClick(selectableItem: SelectableItem<ItemType>) {
        if (selectedItems.isNotEmpty()) {
            return
        }
        val newSelectedItems = HashSet(selectedItems)
        newSelectedItems.add(selectableItem.item)
        changed(items, newSelectedItems)
    }

    fun setItems(items: List<ItemType>) {
        changed(kryo.copy(ArrayList(items)), kryo.copy(HashSet(selectedItems)))
    }

    fun setSelectedItems(value: Set<ItemType>) = changed(kryo.copy(ArrayList(items)), kryo.copy(HashSet(value)))
    fun getSelectedItems() = kryo.copy(selectedItems)
}
