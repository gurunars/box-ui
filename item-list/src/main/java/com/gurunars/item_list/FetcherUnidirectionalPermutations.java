package com.gurunars.item_list;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

class FetcherUnidirectionalPermutations<ItemType extends Item> {

    // if the number of moves exceeds a certain limit - it is cheaper to remove/add specific items
    private final static int moveThreshold = 5;

    @Nullable
    List<ChangeMove<ItemType>> get(List<ItemType> remainedOrderOneOriginal,
                                   List<ItemType> remainedOrderTwo,
                                   boolean reverse) {
        List<ChangeMove<ItemType>> changes = new ArrayList<>();
        List<ItemType> remainedOrderOne = new ArrayList<>(remainedOrderOneOriginal);
        int from = reverse ? remainedOrderOne.size() - 1 : 0;
        while (from < remainedOrderOne.size() && from >= 0) {
            ItemType item = remainedOrderOne.get(from);
            int to = remainedOrderTwo.indexOf(item);

            if (from != to) {
                changes.add(new ChangeMove<>(item, from, to));
                if (changes.size() > moveThreshold) {
                    return null;
                }
                remainedOrderOne.remove(from);
                remainedOrderOne.add(to, item);
                from = reverse ? remainedOrderOne.size() - 1 : 0;
                continue;
            }

            from += reverse ? -1 : +1;
        }

        return changes;
    }
}
