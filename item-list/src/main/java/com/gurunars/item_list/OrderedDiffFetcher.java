package com.gurunars.item_list;

import java.util.ArrayList;
import java.util.List;

import java8.util.function.BiFunction;


class OrderedDiffFetcher<ItemType> implements BiFunction<List<ItemType>, List<ItemType>, List<ItemType>> {
    /**
     * Returns a list of items that are present in one but missing in two. I.e. the items removed
     * from one to create list #2.
     */

    @Override
    public List<ItemType> apply(List<ItemType> one, List<ItemType> two) {
        List<ItemType> missing = new ArrayList<>(one);
        missing.removeAll(two);
        return missing;
    }
}
