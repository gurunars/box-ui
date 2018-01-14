package com.gurunars.item_list

import android.content.Context
import android.view.View
import com.gurunars.box.*
import com.gurunars.box.ui.setAsOne
import com.gurunars.box.ui.statefulView

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
    items: IBox<List<ItemType>>,  // TODO: IRoBox? No retain?
    selectedItems: IBox<Set<ItemType>> = Box(setOf()),
    itemViewBinders: Map<Enum<*>, ItemViewBinder<SelectableItem<ItemType>>> = mapOf(),
    emptyViewBinder: EmptyViewBinder = this::defaultEmptyViewBinder,
    explicitSelectionMode: IRoBox<Boolean> = false.box
): View = statefulView(R.id.selectableItemListView, "SELECTABLE ITEM LIST") {

    val kryo = getKryo()

    retain(
        selectedItems.fork { kryo.copy(HashSet(this)) },
        items.fork { kryo.copy(ArrayList(this)) }
    )

    // The flag is require to prevent selection cleanup during the initialization
    var initialized = false

    val selectables = Box<List<SelectableItem<ItemType>>>(listOf())

    items.onChange(false) { its ->
        if (initialized) {
            selectedItems.patch { filter { its.has(it) }.toSet() }
        }
        initialized = true
        val selected = selectedItems.get()
        selectables.set(its.map { SelectableItem(it, selected.has(it)) })
    }

    selectedItems.onChange { its ->
        selectables.set(items.get().map { SelectableItem(it, its.has(it)) })
    }

    itemListView(
        items = selectables,
        itemViewBinders = itemViewBinders
            .map {
                it.key to ({ item: IRoBox<SelectableItem<ItemType>> ->
                    clickableBind(selectedItems, it.value, item, explicitSelectionMode)
                })
            }.toMap(),
        emptyViewBinder = emptyViewBinder
    ).setAsOne(this)
}
