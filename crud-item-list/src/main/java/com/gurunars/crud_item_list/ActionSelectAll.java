package com.gurunars.crud_item_list;

import android.view.Menu;
import android.view.MenuItem;

import com.gurunars.item_list.Item;

import java.util.List;
import java.util.Set;

import java8.util.function.BiFunction;
import java8.util.function.Function;

final class ActionSelectAll<ItemType extends Item> implements Action<ItemType> {

    private final BiFunction<List<ItemType>, Set<ItemType>, List<Integer>> positionFetcher =
            new PositionFetcher<>();

    private final Function<List<Integer>, Boolean> solidChunkChecker =
            new CheckerSolidChunk();

    @Override
    public MenuItem configureMenuItem(Menu menu) {
        return null;
    }

    @Override
    public void perform(List<ItemType> all, Set<ItemType> selectedItems) {
        selectedItems.addAll(all);
    }

    @Override
    public boolean canPerform(List<ItemType> all, Set<ItemType> selectedItems) {
        return selectedItems.size() < all.size();
    }

}
