package com.gurunars.crud_item_list;

import com.gurunars.item_list.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import java8.util.function.BiFunction;

class Deleter<ItemType extends Item> implements BiFunction<List<ItemType>, Set<ItemType>, List<ItemType>> {

    private final CheckerDelete<ItemType> checker = new CheckerDelete<>();

    @Override
    public List<ItemType> apply(List<ItemType> all, Set<ItemType> selectedItems) {
        if (!checker.apply(all, selectedItems)) {
            return all;
        }
        List<ItemType> listCopy = new ArrayList<>(all);
        listCopy.removeAll(selectedItems);
        return listCopy;
    }

}
