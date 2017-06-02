package com.gurunars.item_list

import com.esotericsoftware.kryo.Kryo
import org.objenesis.strategy.StdInstantiatorStrategy
import java.io.Serializable
import java.util.*


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
        this.items = kryo.copy(ArrayList(newItems))

        fun indexOfItem(items: Collection<ItemType>, item: ItemType): Int {
            return items.indexOfFirst { item.getId() == it.getId() }
        }

        val filteredSelection = kryo.copy(HashSet(newSelection))
                .filter { indexOfItem(items, it) != -1 }
                .map { items[indexOfItem(items, it)] }
                .toHashSet()
        val selectionChanged = selectedItems != filteredSelection
        selectedItems = filteredSelection
        stateChangeHandler(items.map { item -> SelectableItem(item, indexOfItem(selectedItems, item) != -1) })
        if (selectionChanged) {
            selectionChangeListener.run()
        }
    }

    fun itemClick(selectableItem: SelectableItem<ItemType>) {
        if (selectedItems.isEmpty()) {
            return
        }

        val item = selectableItem.item
        changed(items,
                if (selectedItems.contains(item))
                    selectedItems - item
                else
                    selectedItems + item)
    }

    fun itemLongClick(selectableItem: SelectableItem<ItemType>): Boolean {
        if (selectedItems.isEmpty()) {
            changed(items, selectedItems + hashSetOf(selectableItem.item))
        }
        return true
    }

    fun setItems(items: List<ItemType>) { changed(items, selectedItems) }
    fun setSelectedItems(value: Set<ItemType>) = changed(items, value)
    fun getSelectedItems(): Set<ItemType> = kryo.copy(selectedItems)
}
