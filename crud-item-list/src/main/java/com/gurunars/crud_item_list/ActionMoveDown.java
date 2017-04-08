package com.gurunars.crud_item_list;

import java.util.List;
import java.util.Set;

import java8.util.function.BiFunction;
import java8.util.function.Function;

final class ActionMoveDown<ItemType> implements Action<ItemType> {

    private final BiFunction<List<ItemType>, Set<ItemType>, List<Integer>> positionFetcher =
            new PositionFetcher<>();

    private final Function<List<Integer>, Boolean> solidChunkChecker =
            new CheckerSolidChunk();

    @Override
    public boolean perform(final List<ItemType> all, final Set<ItemType> selectedItems) {
        List<Integer> positions = positionFetcher.apply(all, selectedItems);
        int positionToMoveUp = positions.get(positions.size()-1) + 1;
        ItemType itemToMoveUp = all.get(positionToMoveUp);
        all.remove(positionToMoveUp);
        all.add(positions.get(0), itemToMoveUp);
        return true;
    }

    @Override
    public boolean canPerform(List<ItemType> all, Set<ItemType> selectedItems) {
        List<Integer> positions = positionFetcher.apply(all, selectedItems);
        return solidChunkChecker.apply(positions) && positions.get(positions.size() - 1) < all.size() - 1;
    }

}
