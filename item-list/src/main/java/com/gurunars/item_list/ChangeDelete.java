package com.gurunars.item_list;

import android.support.v7.widget.RecyclerView;

import java.util.List;

class ChangeDelete<ItemType extends Item> extends ChangeOfPart<ItemType> {

    ChangeDelete(ItemType item, int sourcePosition, int targetPosition) {
        super(item, sourcePosition, targetPosition);
    }

    @Override
    public int apply(RecyclerView.Adapter adapter, Scroller scroller, List<ItemType> items,
                     int currentPosition) {
        items.remove(sourcePosition);
        adapter.notifyItemRemoved(sourcePosition);
        return currentPosition;
    }
}
