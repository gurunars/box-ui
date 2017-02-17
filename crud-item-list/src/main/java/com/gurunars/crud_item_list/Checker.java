package com.gurunars.crud_item_list;

import java.util.List;
import java.util.Set;

import java8.util.function.BiFunction;

interface Checker<ItemType> extends BiFunction<List<ItemType>, Set<ItemType>, Boolean> {
}
