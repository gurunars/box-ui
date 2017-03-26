package com.gurunars.item_list;

import java.util.ArrayList;
import java.util.List;

class PlainUpdateFetcher<ItemType extends Item> {

    List<Change<ItemType>> get(List<ItemType> sourceList, List<ItemType> targetList, int offset) {
        List<Change<ItemType>> changes = new ArrayList<>();

        for (int i = 0; i < sourceList.size(); i++) {
            ItemType newItem = targetList.get(i);
            int realIndex = offset + i;
            if (!sourceList.get(i).payloadsEqual(newItem)) {
                changes.add(new ChangeUpdate<>(newItem, realIndex, realIndex));
            }
        }

        return changes;
    }


}
