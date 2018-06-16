package com.gurunars.item_list

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.gurunars.box.IRoBox
import com.gurunars.box.ui.*

/**
 * @param ItemType type of the item to be shown in the list
 * @param items A collection of items shown in the list
 * @param itemViewBinders a mapping between item types and view binders meant to render the respective items
 * @param emptyViewBinder a function returning a view to be shown when the list is empty
 */
fun <ItemType : Item> Context.itemListView(
    items: IRoBox<List<ItemType>>,
    itemViewBinders: Map<Enum<*>, ItemViewBinder<ItemType>> = mapOf(),
    emptyViewBinder: EmptyViewBinder = this::defaultEmptyViewBinder
): RecyclerView = with {
    fullSize()
    clipToPadding = false
    padding = OldBounds(bottom = dip(90))
    isSaveEnabled = false
    adapter = ItemAdapter(items, emptyViewBinder, itemViewBinders)
    layoutManager = LinearLayoutManager(context).apply {
        orientation = LinearLayoutManager.VERTICAL
    }
}
