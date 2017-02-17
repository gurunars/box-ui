package com.gurunars.item_list;

import java.util.ArrayList;
import java.util.List;

import java8.util.function.BiFunction;

class OrderedIntersectionFetcher<ItemType> implements BiFunction<List<ItemType>, List<ItemType>, List<ItemType>> {

    @Override
    public List<ItemType> apply(List<ItemType> one, List<ItemType> two) {
        List<ItemType> intersection = new ArrayList<>(one);
        intersection.retainAll(two);
        return intersection;
    }
}
