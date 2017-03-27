package com.gurunars.item_list;

import android.util.Log;

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
        List<ItemType>> diffFetcher =
            new OrderedDiffFetcher<>();

    @Inject
    private
    FetcherPermutations<ItemType> fetcherPermutations = new FetcherPermutations<>();

    @Inject
    private Partitioner<ItemType> partitioner = new Partitioner<>();

    private void verifyNoDuplicates(List<ItemType> items) {
        Set<ItemType> set = new HashSet<>(items);
        if (set.size() != items.size()) {
            throw new RuntimeException("The list of items contains duplicates");
        }
    }

    private List<Change<ItemType>> getUpdates(
            List<ItemType> sourceList, List<ItemType> targetList, int offset) {
        List<Change<ItemType>> changes = new ArrayList<>();

        for (int i=0; i < sourceList.size(); i++) {
            ItemType newItem = targetList.get(i);
            int realIndex = offset + i;
            if (!sourceList.get(i).payloadsEqual(newItem)) {
                changes.add(new ChangeUpdate<>(newItem, realIndex, realIndex));
            }
        }

        return changes;
    }

    private List<ItemType> reverse(List<ItemType> original) {
        Collections.reverse(original);
        return original;
    }

    @Nonnull
    @Override
    public List<Change<ItemType>> apply(List<ItemType> source, List<ItemType> target) {

        verifyNoDuplicates(target);

        TmpTiming timing = new TmpTiming();

        Partitioner.PartitionTuple<ItemType> tuple = partitioner.apply(source, target);

        List<ItemType> sourceMiddle = tuple.getSource().getMiddle();
        List<ItemType> targetMiddle = tuple.getTarget().getMiddle();

        Log.e("SRC", "" + sourceMiddle);
        Log.e("TRG", "" + targetMiddle);

        List<Change<ItemType>> changes = new ArrayList<>();

        //timing.tick("INIT");

        // remove in a reverse order to prevent index recalculation
        for (ItemType item: reverse(diffFetcher.apply(sourceMiddle, targetMiddle))) {
            int position = sourceMiddle.indexOf(item);
            int realIndex = tuple.getStartOffset() + position;
            changes.add(new ChangeDelete<>(item, realIndex, realIndex));
            sourceMiddle.remove(position);
        }

        //timing.tick("REMOVALS");

        for (ItemType item: diffFetcher.apply(targetMiddle, sourceMiddle)) {
            int position = targetMiddle.indexOf(item);
            int realIndex = tuple.getStartOffset() + position;
            changes.add(new ChangeCreate<>(item, realIndex, realIndex));
            sourceMiddle.add(position, item);
        }

        //timing.tick("ADDITIONS");

        // Fetch permutations in both
        changes.addAll(fetcherPermutations.get(sourceMiddle, targetMiddle));

        //timing.tick("MOVES");

        changes.addAll(getUpdates(
                tuple.getSource().getHead(),
                tuple.getTarget().getHead(),
                0)
        );

        Log.e("SRC 2", "" + sourceMiddle);
        Log.e("TRG 2", "" + targetMiddle);

        changes.addAll(getUpdates(
                sourceMiddle,
                targetMiddle,
                tuple.getStartOffset())
        );
        changes.addAll(getUpdates(
                tuple.getSource().getTail(),
                tuple.getTarget().getTail(),
                tuple.getStartOffset() + sourceMiddle.size())
        );

        //timing.tick("UPDATES");

        return changes;
    }
}
