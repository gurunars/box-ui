package com.gurunars.item_list

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.gurunars.box.IBox
import com.gurunars.box.ui.fullSize
import com.gurunars.box.oneWayBranch
import org.jetbrains.anko.bottomPadding
import org.jetbrains.anko.dip

/**
 * @param ItemType type of the item to be shown in the list
 * @param items A collection of items shown in the list
 * @param itemViewBinders a mapping between item types and view binders
 * meant  to render the respective items
 * @param emptyViewBinder a function returning a view to be shown when the list is empty
 * @param stableIds - if false, forces the whole list to be updated whenever the changes arrive
 */
fun <ItemType : Item> Context.itemListView(
    items: IBox<List<ItemType>>,
    itemViewBinders: Map<Enum<*>, ItemViewBinder<ItemType>> = mapOf(),
    emptyViewBinder: EmptyViewBinder = this::defaultBindEmpty,
    stableIds: Boolean = true
): View = RecyclerView(this).apply {
    val kryo = getKryo()
    id = R.id.recyclerView
    fullSize()
    clipToPadding = false
    bottomPadding = dip(60)
    isSaveEnabled = false
    adapter = ItemAdapter(
        items.oneWayBranch {
            kryo.copy(if (stableIds) distinctBy { it.id } else this)
        },
        emptyViewBinder,
        itemViewBinders
    ).apply {
        setHasStableIds(stableIds)
    }
    layoutManager = LinearLayoutManager(context).apply {
        orientation = LinearLayoutManager.VERTICAL
    }
}
