package com.gurunars.item_list;

import android.support.v7.widget.RecyclerView;

import java.util.List;

import javax.inject.Inject;

class ChangeMove<ItemType extends Item> extends ChangeOfPart<ItemType> {

    @Inject
    private ScrollPositionFetcher scrollPositionFetcher = new ScrollPositionFetcher();

    ChangeMove(ItemType item, int sourcePosition, int targetPosition) {
        super(item, sourcePosition, targetPosition);
    }

    @Override
    public int apply(RecyclerView.Adapter adapter, Scroller scroller, List<ItemType> items,
                     int currentPosition) {
        items.remove(sourcePosition);
        items.add(targetPosition, item);
        adapter.notifyItemMoved(sourcePosition, targetPosition);
        return scrollPositionFetcher.getScrollPosition(sourcePosition, scroller, items);
    }
}
