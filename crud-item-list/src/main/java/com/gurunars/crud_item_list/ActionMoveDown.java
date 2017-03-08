package com.gurunars.crud_item_list;

import android.view.Menu;
import android.view.MenuItem;

import com.gurunars.item_list.Item;

import java.util.List;
import java.util.Set;

import java8.util.function.BiFunction;
import java8.util.function.Function;

final class ActionMoveDown<ItemType extends Item> implements Action<ItemType> {

    final BiFunction<List<ItemType>, Set<ItemType>, List<Integer>> positionFetcher =
            new PositionFetcher<>();

    final Function<List<Integer>, Boolean> solidChunkChecker =
            new CheckerSolidChunk();

    @Override
    public MenuItem configureMenuItem(Menu menu) {
        return null;
    }

    @Override
    public void perform(List<ItemType> all, Set<ItemType> selectedItems) {
        List<Integer> positions = positionFetcher.apply(all, selectedItems);
        int positionToMoveUp = positions.get(positions.size()-1) + 1;
        ItemType itemToMoveUp = all.get(positionToMoveUp);
        all.remove(positionToMoveUp);
        all.add(positions.get(0), itemToMoveUp);
    }

    @Override
    public boolean canPerform(List<ItemType> all, Set<ItemType> selectedItems) {
        List<Integer> positions = positionFetcher.apply(all, selectedItems);
        return solidChunkChecker.apply(positions) && positions.get(positions.size() - 1) < all.size() - 1;
    }

}
