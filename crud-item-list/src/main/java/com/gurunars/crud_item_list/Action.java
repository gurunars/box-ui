package com.gurunars.crud_item_list;

import java.util.List;
import java.util.Set;

interface Action<ItemType> {

    boolean perform(List<ItemType> all, Set<ItemType> selectedItems);
    boolean canPerform(List<ItemType> all, Set<ItemType> selectedItems);

}
