package com.gurunars.item_list;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import java8.util.function.BiFunction;


class Differ<ItemType extends Item> implements BiFunction<List<ItemType>, List<ItemType>, List<Change<ItemType>>> {

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
    FetcherPermutations<ItemType> fetcherPermutations = new FetcherPermutations<>();

    @Nonnull
    @Override
    public List<Change<ItemType>> apply(List<ItemType> source, List<ItemType> target) {

        List<ItemHolder<ItemType>> sourceList = ItemHolder.wrap(source);
        List<ItemHolder<ItemType>> targetList = ItemHolder.wrap(target);

        List<ItemHolder<ItemType>> removed = diffFetcher.apply(sourceList, targetList);
        Collections.reverse(removed);  // remove in a reverse order to prevent index recalculation

        List<Change<ItemType>> changes = new ArrayList<>();

        for (ItemHolder<ItemType> item: removed) {
            int position = sourceList.indexOf(item);
            changes.add(new ChangeDelete<>(item.getRaw(), position, position));
            sourceList.remove(position);
        }

        List<ItemHolder<ItemType>> added = diffFetcher.apply(targetList, sourceList);

        for (ItemHolder<ItemType> item: added) {
            int position = targetList.indexOf(item);
            changes.add(new ChangeCreate<>(item.getRaw(), position, position));
            sourceList.add(position, item);
        }

        List<ItemHolder<ItemType>> intersectionSourceOrder =
                intersectionFetcher.apply(sourceList, targetList);
        List<ItemHolder<ItemType>> intersectionTargetOrder =
                intersectionFetcher.apply(targetList, sourceList);

        changes.addAll(
                fetcherPermutations.get(intersectionSourceOrder, intersectionTargetOrder));

        for (ItemHolder<ItemType> item: sourceList) {
            int index = targetList.indexOf(item);
            ItemHolder<ItemType> newItem = targetList.get(index);
            if (!item.getRaw().equals(newItem.getRaw())) {
                changes.add(new ChangeUpdate<>(newItem.getRaw(), index, index));
            }
        }

        return changes;
    }
}
