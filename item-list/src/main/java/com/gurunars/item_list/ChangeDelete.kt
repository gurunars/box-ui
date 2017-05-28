package com.gurunars.item_list

import android.support.v7.widget.RecyclerView

internal class ChangeDelete<ItemType : Item>(item: ItemType, sourcePosition: Int, targetPosition: Int) : ChangeOfPart<ItemType>(item, sourcePosition, targetPosition) {

    override fun apply(adapter: RecyclerView.Adapter<*>, scroller: Scroller, items: MutableList<ItemType>,
                       currentPosition: Int): Int {
        items.removeAt(sourcePosition)
        adapter.notifyItemRemoved(sourcePosition)
        return currentPosition
    }
}
