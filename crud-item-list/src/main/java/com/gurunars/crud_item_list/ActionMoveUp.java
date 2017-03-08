package com.gurunars.crud_item_list;

import java.util.List;
import java.util.Set;

import java8.util.function.BiFunction;
import java8.util.function.Function;

final class ActionMoveUp<ItemType> implements Action<ItemType> {

    private final BiFunction<List<ItemType>, Set<ItemType>, List<Integer>> positionFetcher =
            new PositionFetcher<>();

    private final Function<List<Integer>, Boolean> solidChunkChecker =
            new CheckerSolidChunk();

    @Override
    public void perform(List<ItemType> all, Set<ItemType> selectedItems) {
        List<Integer> positions = positionFetcher.apply(all, selectedItems);
        int positionToMoveDown = positions.get(0) - 1;
        ItemType itemToMoveDown = all.get(positionToMoveDown);
        all.add(positions.get(positions.size() - 1) +1, itemToMoveDown);
        all.remove(positionToMoveDown);
    }

    @Override
    public boolean canPerform(List<ItemType> all, Set<ItemType> selectedItems) {
        List<Integer> positions = positionFetcher.apply(all, selectedItems);
        return solidChunkChecker.apply(positions) && positions.get(0) > 0;
    }

}
