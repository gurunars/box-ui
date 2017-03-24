package com.gurunars.item_list;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import static com.gurunars.item_list.ItemViewBinderEmpty.EMPTY_TYPE;

class BindableViewHolder<ViewType extends View, ItemType extends Item> extends RecyclerView.ViewHolder {

    private ItemViewBinder<ViewType, ItemType> itemViewBinder;

    BindableViewHolder(ViewGroup root,
                       ItemViewBinder<ViewType, ItemType> itemViewBinder) {
        super(itemViewBinder.getView(root.getContext()));
        this.itemViewBinder = itemViewBinder;
        itemView.setLayoutParams(new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    BindableViewHolder(ViewGroup root,
                       EmptyViewBinder emptyViewBinder) {
        super(emptyViewBinder.getView(root.getContext()));
        this.itemViewBinder = null;
        itemView.setTag(EMPTY_TYPE, EMPTY_TYPE);
        itemView.setLayoutParams(new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
    }

    void bind(ItemType item, @Nullable ItemType previousItem) {
        if (this.itemViewBinder != null) {
            itemViewBinder.bind((ViewType) itemView, item, previousItem);
        }
    }
}
