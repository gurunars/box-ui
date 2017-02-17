package com.gurunars.item_list;

import android.support.v7.widget.RecyclerView;

import java.util.List;

class ChangePersist<ItemType extends Item> implements Change<ItemType> {

    @Override
    public int apply(RecyclerView.Adapter adapter, Scroller scroller, List<ItemType> items,
                     int currentPosition) {
        // Do nothing
        return currentPosition;
    }

}
