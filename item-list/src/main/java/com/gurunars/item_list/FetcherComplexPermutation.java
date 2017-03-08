package com.gurunars.item_list;

import java.util.List;

import java8.util.function.BiFunction;


class FetcherComplexPermutation<ItemType extends Item> implements BiFunction<
        List<ItemType>, List<ItemType>, Change<ItemType>> {

    private FetcherPermutationRange<ItemType> fetcher = new FetcherPermutationRange<>();

    public Change<ItemType> apply(
            List<ItemType> source,
            List<ItemType> target
    ) {
        FetcherPermutationRange.Range range = fetcher.apply(source, target);
        return new ChangeComplexPermutation<>(
                range.getStart(),
                target.subList(range.getStart(), range.getEnd()));
    }
}
