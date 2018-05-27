package com.gurunars.item_list

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.gurunars.box.IRoBox
import com.gurunars.box.box
import com.gurunars.box.ui.Bounds
import com.gurunars.box.ui.dip
import com.gurunars.box.ui.fullSize
import com.gurunars.box.ui.padding

/**
 * @param ItemType type of the item to be shown in the list
 * @param items A collection of items shown in the list
 * @param itemViewBinders a mapping between item types and view binders meant to render the respective items
 * @param emptyViewBinder a function returning a view to be shown when the list is empty
 */
fun <ItemType : Item> Context.itemListView(
    items: IRoBox<List<ItemType>>,
    itemViewBinders: Map<Enum<*>, ItemViewBinder<ItemType>> = mapOf(),
    emptyViewBinder: EmptyViewBinder = this::defaultEmptyViewBinder,
    reverseLayout: IRoBox<Boolean> = false.box
): View = RecyclerView(this).apply {
    id = R.id.recyclerView
    fullSize()
    clipToPadding = false
    padding = Bounds(bottom=dip(60))
    isSaveEnabled = false
    adapter = ItemAdapter(
        items,
        emptyViewBinder,
        itemViewBinders
    )
    layoutManager = LinearLayoutManager(context).apply {
        orientation = LinearLayoutManager.VERTICAL
        reverseLayout.onChange { this.reverseLayout = it }
    }
}
