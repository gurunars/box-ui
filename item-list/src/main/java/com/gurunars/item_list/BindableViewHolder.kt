package com.gurunars.item_list

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.gurunars.item_list.ItemViewBinderEmpty.Companion.EMPTY_TYPE
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.wrapContent


internal class BindableViewHolder<ViewType : View, ItemType : Item> : RecyclerView.ViewHolder {

    private var itemViewBinder: ItemViewBinder<ViewType, ItemType>? = null

    constructor(root: ViewGroup,
                itemViewBinder: ItemViewBinder<ViewType, ItemType>) : super(itemViewBinder.getView(root.context)) {
        this.itemViewBinder = itemViewBinder
        itemView.layoutParams = RecyclerView.LayoutParams(matchParent, wrapContent)
    }

    constructor(root: ViewGroup,
                emptyViewBinder: EmptyViewBinder) : super(emptyViewBinder.getView(root.context)) {
        this.itemViewBinder = null
        itemView.apply {
            setTag(EMPTY_TYPE, EMPTY_TYPE)
            layoutParams = RecyclerView.LayoutParams(matchParent, matchParent)
        }
    }

    fun bind(item: ItemType, previousItem: ItemType?) {
        itemViewBinder?.bind(itemView as ViewType, item, previousItem)
    }
}
