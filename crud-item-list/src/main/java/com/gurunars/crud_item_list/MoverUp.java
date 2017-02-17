package com.gurunars.crud_item_list;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import java8.util.function.BiFunction;

class MoverUp<ItemType> implements BiFunction<List<ItemType>, Set<ItemType>, List<ItemType>> {

    private final PositionFetcher<ItemType> positionFetcher = new PositionFetcher<>();
    private final CheckerMoveUp<ItemType> checker = new CheckerMoveUp<>();

    @Override
    public List<ItemType> apply(List<ItemType> all, Set<ItemType> selectedItems) {
        if (!checker.apply(all, selectedItems)) {
            return all;
        }
        List<ItemType> newItems = new ArrayList<>();
        newItems.addAll(all);
        List<Integer> positions = positionFetcher.apply(all, selectedItems);

        int positionToMoveDown = positions.get(0) - 1;
        ItemType itemToMoveDown = all.get(positionToMoveDown);

        newItems.add(positions.get(positions.size() - 1) +1, itemToMoveDown);
        newItems.remove(positionToMoveDown);

        return newItems;
    }
}
