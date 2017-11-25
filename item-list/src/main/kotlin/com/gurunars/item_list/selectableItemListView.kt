package com.gurunars.item_list

import android.content.Context
import android.view.View
import com.gurunars.databinding.*
import com.gurunars.databinding.android.setAsOne
import com.gurunars.databinding.android.statefulView
import java.util.*
import kotlin.collections.HashSet

/**
 * Item list that has selection enabled.
 *
 * Items can be selected initially via long clicking and further on by consequentially
 * clicking them.
 *
 * @param ItemType type of the item to be shown in the list
 * @param items A collection of items shown in the list
 * @param selectedItems A collection of items selected at the moment
 * @param itemViewBinders a type based mapping between item type and item renderer
 * @param emptyViewBinder a function returning a view to be shown when the list is empty
 * @param explicitSelectionMode when true selection mode is initiated via normal click instead of a long one
 */
fun <ItemType : Item> Context.selectableItemListView(
    items: BindableField<List<ItemType>>,
    selectedItems: BindableField<Set<ItemType>> = BindableField(setOf()),
    itemViewBinders: Map<Enum<*>, ItemViewBinder<SelectableItem<ItemType>>> = mapOf(),
    emptyViewBinder: EmptyViewBinder = this::defaultBindEmpty,
    explicitSelectionMode: BindableField<Boolean> = false.field
): View = statefulView(R.id.selectableItemListView, "SELECTABLE ITEM LIST") {

    val kryo = getKryo()

    val copyOfSelectedItems: BindableField<Set<ItemType>> =
        selectedItems.fork { kryo.copy(HashSet(this)) }

    val copyOfItems: BindableField<List<ItemType>> =
        items.fork { kryo.copy(ArrayList(this)) }

    retain(copyOfSelectedItems)

    val selectables = BindableField<List<SelectableItem<ItemType>>>(listOf())

    listOf(copyOfItems, copyOfSelectedItems).onChange {
        val its = copyOfItems.get()
        copyOfSelectedItems.patch { filter { its.has(it) }.toSet() }
        selectables.set(its.map { SelectableItem(it, copyOfSelectedItems.get().has(it)) })
    }

    itemListView(
        items = selectables,
        itemViewBinders = itemViewBinders.
            map {
                it.key to ({ item: BindableField<SelectableItem<ItemType>> ->
                    clickableBind(copyOfSelectedItems, it.value, item, explicitSelectionMode)
                })
            }.toMap(),
        emptyViewBinder = emptyViewBinder
    ).setAsOne(this)
}
