package com.gurunars.item_list;

import java.util.List;

import java8.util.function.BiFunction;


class FetcherComplexPermutation<ItemType extends Item> {

    private FetcherPermutationRange<ItemType> fetcher = new FetcherPermutationRange<>();

    public Change<ItemType> apply(
            List<ItemType> source,
            List<ItemType> target,
            int startOffset
    ) {
        FetcherPermutationRange.Range range = fetcher.apply(source, target);
        return new ChangeComplexPermutation<>(
                startOffset + range.getStart(),
                target.subList(range.getStart(), range.getEnd()));
    }
}
