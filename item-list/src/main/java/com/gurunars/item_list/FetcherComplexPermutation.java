package com.gurunars.item_list;

import java.util.List;

import java8.util.function.BiFunction;


class FetcherComplexPermutation<ItemType extends Item> implements BiFunction<
        List<ItemHolder<ItemType>>, List<ItemHolder<ItemType>>, Change<ItemType>> {

    private FetcherPermutationRange<ItemHolder<ItemType>> fetcher = new FetcherPermutationRange<>();

    public Change<ItemType> apply(
            List<ItemHolder<ItemType>> source,
            List<ItemHolder<ItemType>> target
    ) {
        FetcherPermutationRange.Range range = fetcher.apply(source, target);
        return new ChangeComplexPermutation<>(
                range.getStart(),
                ItemHolder.unwrap(target.subList(range.getStart(), range.getEnd())));
    }
}
