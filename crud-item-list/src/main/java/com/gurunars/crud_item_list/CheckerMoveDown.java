package com.gurunars.crud_item_list;

import java.util.List;
import java.util.Set;

class CheckerMoveDown<ItemType> extends CheckerMove<ItemType> {

    @Override
    public Boolean apply(List<ItemType> all, Set<ItemType> selectedItems) {
        List<Integer> positions = positionFetcher.apply(all, selectedItems);
        return solidChunkChecker.apply(positions) && positions.get(positions.size() - 1) < all.size() - 1;
    }
}
