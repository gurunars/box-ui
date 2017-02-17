package com.gurunars.crud_item_list;

import java.util.List;
import java.util.Set;

import java8.util.function.BiFunction;
import java8.util.function.Function;

abstract class CheckerMove<ItemType> implements Checker<ItemType> {

    final BiFunction<List<ItemType>, Set<ItemType>, List<Integer>> positionFetcher =
        new PositionFetcher<>();

    final Function<List<Integer>, Boolean> solidChunkChecker =
        new CheckerSolidChunk();

}
