package com.gurunars.item_list;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java8.util.function.BiFunction;


class Differ<ItemType extends Item> implements BiFunction<List<ItemHolder<ItemType>>, List<ItemHolder<ItemType>>, List<Change<ItemHolder<ItemType>>>> {

    @Inject
    private
    BiFunction<
        List<ItemHolder<ItemType>>,
        List<ItemHolder<ItemType>>,
        List<ItemHolder<ItemType>>> intersectionFetcher =
            new OrderedIntersectionFetcher<>();

    @Inject
    private
    BiFunction<
        List<ItemHolder<ItemType>>,
        List<ItemHolder<ItemType>>,
        List<ItemHolder<ItemType>>> diffFetcher =
            new OrderedDiffFetcher<>();

    @Inject
    private
    FetcherPermutations<ItemHolder<ItemType>> fetcherPermutations = new FetcherPermutations<>();

    private void verifyNoDuplicates(List<ItemHolder<ItemType>> items) {
        Set<ItemHolder<ItemType>> set = new HashSet<>(items);
        if (set.size() != items.size()) {
            throw new RuntimeException("The list of items contains duplicates");
        }
    }

    @Nonnull
    @Override
    public List<Change<ItemHolder<ItemType>>> apply(List<ItemHolder<ItemType>> source, List<ItemHolder<ItemType>> target) {

        List<ItemHolder<ItemType>> sourceList = new ArrayList<>(source);
        List<ItemHolder<ItemType>> targetList = new ArrayList<>(target);

        verifyNoDuplicates(sourceList);

        List<ItemHolder<ItemType>> removed = diffFetcher.apply(sourceList, targetList);
        Collections.reverse(removed);  // remove in a reverse order to prevent index recalculation

        List<Change<ItemHolder<ItemType>>> changes = new ArrayList<>();

        for (ItemHolder<ItemType> item: removed) {
            int position = sourceList.indexOf(item);
            changes.add(new ChangeDelete<>(item, position, position));
            sourceList.remove(position);
        }

        List<ItemHolder<ItemType>> added = diffFetcher.apply(targetList, sourceList);

        for (ItemHolder<ItemType> item: added) {
            int position = targetList.indexOf(item);
            changes.add(new ChangeCreate<>(item, position, position));
            sourceList.add(position, item);
        }

        List<ItemHolder<ItemType>> intersectionSourceOrder =
                intersectionFetcher.apply(sourceList, targetList);
        List<ItemHolder<ItemType>> intersectionTargetOrder =
                intersectionFetcher.apply(targetList, sourceList);

        changes.addAll(fetcherPermutations.get(intersectionSourceOrder, intersectionTargetOrder));

        for (ItemHolder<ItemType> item: sourceList) {
            int index = targetList.indexOf(item);
            ItemHolder<ItemType> newItem = targetList.get(index);
            if (!item.getRaw().equals(newItem.getRaw())) {
                changes.add(new ChangeUpdate<>(newItem, index, index));
            }
        }

        return changes;
    }
}
