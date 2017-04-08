package com.gurunars.crud_item_list;

import java.util.List;
import java.util.Set;

final class ActionSelectAll<ItemType> implements Action<ItemType> {

    @Override
    public boolean perform(List<ItemType> all, Set<ItemType> selectedItems) {
        selectedItems.addAll(all);
        return true;
    }

    @Override
    public boolean canPerform(List<ItemType> all, Set<ItemType> selectedItems) {
        return selectedItems.size() < all.size();
    }

}
