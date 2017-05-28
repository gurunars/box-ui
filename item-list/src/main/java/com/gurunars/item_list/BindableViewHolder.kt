package com.gurunars.item_list

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.gurunars.item_list.ItemViewBinderEmpty.Companion.EMPTY_TYPE


internal class BindableViewHolder<ViewType : View, ItemType : Item> : RecyclerView.ViewHolder {

    private var itemViewBinder: ItemViewBinder<ViewType, ItemType>? = null

    constructor(root: ViewGroup,
                itemViewBinder: ItemViewBinder<ViewType, ItemType>) : super(itemViewBinder.getView(root.context)) {
        this.itemViewBinder = itemViewBinder
        itemView.layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    constructor(root: ViewGroup,
                emptyViewBinder: EmptyViewBinder) : super(emptyViewBinder.getView(root.context)) {
        this.itemViewBinder = null
        itemView.setTag(EMPTY_TYPE, EMPTY_TYPE)
        itemView.layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
    }

    fun bind(item: ItemType, previousItem: ItemType?) {
        itemViewBinder?.bind(itemView as ViewType, item, previousItem)
    }
}
