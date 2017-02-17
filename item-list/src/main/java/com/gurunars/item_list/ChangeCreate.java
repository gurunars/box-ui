package com.gurunars.item_list;

import android.support.v7.widget.RecyclerView;

import java.util.List;

import javax.inject.Inject;

class ChangeCreate<ItemType extends Item> extends ChangeOfPart<ItemType> {

    @Inject
    private ScrollPositionFetcher scrollPositionFetcher = new ScrollPositionFetcher();

    ChangeCreate(ItemType item, int sourcePosition, int targetPosition) {
        super(item, sourcePosition, targetPosition);
    }

    @Override
    public int apply(RecyclerView.Adapter adapter, Scroller scroller, List<ItemType> items,
                     int currentPosition) {
        items.add(sourcePosition, item);
        adapter.notifyItemInserted(sourcePosition);
        return scrollPositionFetcher.getScrollPosition(sourcePosition, scroller, items);
    }


}
