package com.gurunars.crud_item_list;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import java8.util.function.BiFunction;

class PositionFetcher<ItemType> implements BiFunction<List<ItemType>, Set<ItemType>, List<Integer>> {

    @Override
    public List<Integer> apply(List<ItemType> all, Set<ItemType> selectedItems) {
        List<Integer> positions = new ArrayList<>();

        if (all.isEmpty() || selectedItems.isEmpty()) {
            return positions;
        }

        for (ItemType cursor: selectedItems) {
            int index = all.indexOf(cursor);
            if (index != -1) {
                positions.add(index);
            }
        }
        Collections.sort(positions);
        return positions;
    }
}
