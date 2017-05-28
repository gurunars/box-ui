package com.gurunars.item_list

import android.support.v7.widget.RecyclerView

import javax.inject.Inject

internal class ChangeMove<ItemType : Item>(item: ItemType, sourcePosition: Int, targetPosition: Int) : ChangeOfPart<ItemType>(item, sourcePosition, targetPosition) {

    @Inject
    private val scrollPositionFetcher = ScrollPositionFetcher()

    override fun apply(adapter: RecyclerView.Adapter<*>, scroller: Scroller, items: MutableList<ItemType>,
                       currentPosition: Int): Int {
        items.removeAt(sourcePosition)
        items.add(targetPosition, item)
        adapter.notifyItemMoved(sourcePosition, targetPosition)
        return scrollPositionFetcher.getScrollPosition(sourcePosition, scroller, items)
    }
}
