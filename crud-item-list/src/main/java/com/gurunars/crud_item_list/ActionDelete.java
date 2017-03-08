package com.gurunars.crud_item_list;

import java.util.List;
import java.util.Set;

final class ActionDelete<ItemType> implements Action<ItemType> {

    @Override
    public void perform(List<ItemType> all, Set<ItemType> selectedItems) {
        all.removeAll(selectedItems);
    }

    @Override
    public boolean canPerform(List<ItemType> all, Set<ItemType> selectedItems) {
        return selectedItems.size() > 0;
    }

}
