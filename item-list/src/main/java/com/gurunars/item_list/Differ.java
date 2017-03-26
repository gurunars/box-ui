package com.gurunars.item_list;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java8.util.function.BiFunction;


class Differ<ItemType extends Item> implements BiFunction<List<ItemType>, List<ItemType>, List<Change<ItemType>>> {

    @Inject
    private
    BiFunction<
        List<ItemType>,
        List<ItemType>,
        List<ItemType>> intersectionFetcher =
            new OrderedIntersectionFetcher<>();

    @Inject
    private
    BiFunction<
        List<ItemType>,
        List<ItemType>,
        List<ItemType>> diffFetcher =
            new OrderedDiffFetcher<>();

    @Inject
    private
    FetcherPermutations<ItemType> fetcherPermutations = new FetcherPermutations<>();

    private void verifyNoDuplicates(List<ItemType> items) {
        Set<ItemType> set = new HashSet<>(items);
        if (set.size() != items.size()) {
            throw new RuntimeException("The list of items contains duplicates");
        }
    }

    @Nonnull
    @Override
    public List<Change<ItemType>> apply(List<ItemType> source, List<ItemType> target) {

        TmpTiming timing = new TmpTiming();

        List<ItemType> sourceList = new ArrayList<>(source);
        List<ItemType> targetList = new ArrayList<>(target);

        verifyNoDuplicates(sourceList);

        timing.tick("INIT");

        List<ItemType> removed = diffFetcher.apply(sourceList, targetList);
        Collections.reverse(removed);  // remove in a reverse order to prevent index recalculation

        timing.tick("REMOVED FIND");

        List<Change<ItemType>> changes = new ArrayList<>();

        for (ItemType item: removed) {
            int position = sourceList.indexOf(item);
            changes.add(new ChangeDelete<>(item, position, position));
            sourceList.remove(position);
        }

        timing.tick("REMOVED CHANGES");

        List<ItemType> added = diffFetcher.apply(targetList, sourceList);

        timing.tick("ADDED FIND");

        for (ItemType item: added) {
            int position = targetList.indexOf(item);
            changes.add(new ChangeCreate<>(item, position, position));
            sourceList.add(position, item);
        }

        timing.tick("ADDED CHANGES");

        List<ItemType> intersectionSourceOrder =
                intersectionFetcher.apply(sourceList, targetList);
        timing.tick("INTERS OD 1");
        List<ItemType> intersectionTargetOrder =
                intersectionFetcher.apply(targetList, sourceList);
        timing.tick("INTERS OD 2");

        changes.addAll(fetcherPermutations.get(intersectionSourceOrder, intersectionTargetOrder));

        timing.tick("PERMUTATIONS FIND");

        for (ItemType item: sourceList) {
            int index = targetList.indexOf(item);
            ItemType newItem = targetList.get(index);
            if (!item.payloadsEqual(newItem)) {
                changes.add(new ChangeUpdate<>(newItem, index, index));
            }
        }

        timing.tick("PERMUTATIONS CHANGES");

        return changes;
    }
}
