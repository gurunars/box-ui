package com.gurunars.item_list;

import java.util.ArrayList;
import java.util.List;

class MutatedUpdateFetcher<ItemType extends Item> {

    List<? extends Change<ItemType>> get(List<ItemType> sourceMiddle,
                                         List<ItemType> targetMiddle,
                                         int startOffset) {
        List<Change<ItemType>> changes = new ArrayList<>();

        for (ItemType sourceItem : sourceMiddle) {
            int index = targetMiddle.indexOf(sourceItem);
            int realIndex = startOffset + index;
            ItemType targetItem = targetMiddle.get(index);
            if (!sourceItem.payloadsEqual(targetItem)) {
                changes.add(new ChangeUpdate<>(targetItem, realIndex, realIndex));
            }
        }

        return changes;
    }

}
