package com.gurunars.item_list

import android.support.v7.widget.RecyclerView

internal class ChangePersist<ItemType : Item> : Change<ItemType> {

    override fun apply(adapter: RecyclerView.Adapter<*>, scroller: Scroller, items: MutableList<ItemType>,
                       currentPosition: Int): Int {
        // Do nothing
        return currentPosition
    }

}
