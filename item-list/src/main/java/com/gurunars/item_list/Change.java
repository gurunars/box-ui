package com.gurunars.item_list;

import android.support.v7.widget.RecyclerView;

import java.util.List;

interface Change<ItemType extends Item> {
    int apply(
            RecyclerView.Adapter adapter,
            Scroller scroller,
            List<ItemType> items,
            int currentPosition);
}
