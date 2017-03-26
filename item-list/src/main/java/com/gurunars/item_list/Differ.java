package com.gurunars.item_list;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

import java8.util.function.BiFunction;


class Differ<ItemType extends Item> implements BiFunction<List<ItemType>, List<ItemType>, List<Change<ItemType>>> {

    private BiFunction<List<ItemType>, List<ItemType>, List<ItemType>> diffFetcher = new OrderedDiffFetcher<>();
    private FetcherPermutations<ItemType> fetcherPermutations = new FetcherPermutations<>();
    private Partitioner<ItemType> partitioner = new Partitioner<>();
    private PlainUpdateFetcher<ItemType> plainUpdateFetcher = new PlainUpdateFetcher<>();
    private MutatedUpdateFetcher<ItemType> mutatedUpdateFetcher = new MutatedUpdateFetcher<>();

    private void verifyNoDuplicates(List<ItemType> items) {
        Set<ItemType> set = new HashSet<>(items);
        if (set.size() != items.size()) {
            throw new RuntimeException("The list of items contains duplicates");
        }
    }

    private List<ItemType> reverse(List<ItemType> original) {
        Collections.reverse(original);
        return original;
    }

    @Nonnull
    @Override
    public List<Change<ItemType>> apply(List<ItemType> source, List<ItemType> target) {

        verifyNoDuplicates(target);

        Partitioner.PartitionTuple<ItemType> tuple = partitioner.apply(source, target);

        List<ItemType> sourceMiddle = tuple.getSource().getMiddle();
        List<ItemType> targetMiddle = tuple.getTarget().getMiddle();

        List<Change<ItemType>> changes = new ArrayList<>();

        // remove in a reverse order to prevent index recalculation
        for (ItemType item: reverse(diffFetcher.apply(sourceMiddle, targetMiddle))) {
            int position = sourceMiddle.indexOf(item);
            int realIndex = tuple.getStartOffset() + position;
            changes.add(new ChangeDelete<>(item, realIndex, realIndex));
            sourceMiddle.remove(position);
        }

        for (ItemType item: diffFetcher.apply(targetMiddle, sourceMiddle)) {
            int position = targetMiddle.indexOf(item);
            int realIndex = tuple.getStartOffset() + position;
            changes.add(new ChangeCreate<>(item, realIndex, realIndex));
            sourceMiddle.add(position, item);
        }

        // Fetch permutations in both
        changes.addAll(fetcherPermutations.get(sourceMiddle, targetMiddle, tuple.getStartOffset()));

        changes.addAll(plainUpdateFetcher.get(
                tuple.getSource().getHead(),
                tuple.getTarget().getHead(),
                0)
        );

        changes.addAll(mutatedUpdateFetcher.get(
                sourceMiddle,
                targetMiddle,
                tuple.getStartOffset())
        );

        changes.addAll(plainUpdateFetcher.get(
                tuple.getSource().getTail(),
                tuple.getTarget().getTail(),
                tuple.getStartOffset() + sourceMiddle.size())
        );

        return changes;
    }

}
