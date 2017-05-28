package com.gurunars.item_list

import android.support.v7.widget.RecyclerView

internal interface Change<ItemType : Item> {
    fun apply(
            adapter: RecyclerView.Adapter<*>,
            scroller: Scroller,
            items: MutableList<ItemType>,
            currentPosition: Int): Int
}
