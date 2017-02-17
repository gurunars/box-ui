package com.gurunars.crud_item_list;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import java8.util.function.BiFunction;

class MoverDown<ItemType> implements BiFunction<List<ItemType>, Set<ItemType>, List<ItemType>> {

    private final PositionFetcher<ItemType> positionFetcher = new PositionFetcher<>();
    private final CheckerMoveDown<ItemType> checker = new CheckerMoveDown<>();

    @Override
    public List<ItemType> apply(List<ItemType> all, Set<ItemType> selectedItems) {
        if (!checker.apply(all, selectedItems)) {
            return all;
        }
        List<ItemType> newItems = new ArrayList<>();
        newItems.addAll(all);
        List<Integer> positions = positionFetcher.apply(all, selectedItems);

        int positionToMoveUp = positions.get(positions.size()-1) + 1;
        ItemType itemToMoveUp = all.get(positionToMoveUp);

        newItems.remove(positionToMoveUp);
        newItems.add(positions.get(0), itemToMoveUp);

        return newItems;
    }
}
