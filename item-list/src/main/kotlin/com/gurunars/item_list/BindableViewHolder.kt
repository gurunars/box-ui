package com.gurunars.item_list

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.gurunars.item_list.ItemViewBinderEmpty.Companion.EMPTY_TYPE
import com.gurunars.shortcuts.asRow
import com.gurunars.shortcuts.fullSize


internal class BindableViewHolder<ViewType : View, ItemType : Item> : RecyclerView.ViewHolder {

    private var itemViewBinder: ItemViewBinder<ViewType, ItemType>? = null

    constructor(root: ViewGroup,
                itemViewBinder: ItemViewBinder<ViewType, ItemType>) : super(itemViewBinder.getView(root.context)) {
        this.itemViewBinder = itemViewBinder
        itemView.asRow()
    }

    constructor(root: ViewGroup,
                emptyViewBinder: EmptyViewBinder) : super(emptyViewBinder.getView(root.context)) {
        this.itemViewBinder = null
        itemView.apply {
            setTag(EMPTY_TYPE, EMPTY_TYPE)
            this.fullSize()
        }
    }

    fun bind(item: ItemType, previousItem: ItemType?) {
        itemViewBinder?.bind(itemView as ViewType, item, previousItem)
    }
}
