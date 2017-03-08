package com.gurunars.item_list;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import static com.gurunars.item_list.ItemViewBinderEmpty.EMPTY_TYPE;

class BindableViewHolder<PayloadType extends Payload> extends RecyclerView.ViewHolder {

    private ItemViewBinder<PayloadType> itemViewBinder;

    BindableViewHolder(ViewGroup root,
                       ItemViewBinder<PayloadType> itemViewBinder) {
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

    void bind(Item<PayloadType> item, @Nullable Item<PayloadType> previousItem) {
        if (this.itemViewBinder != null) {
            itemViewBinder.bind(itemView, item, previousItem);
        }
    }
}
