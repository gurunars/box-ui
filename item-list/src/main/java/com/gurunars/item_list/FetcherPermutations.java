package com.gurunars.item_list;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import java8.util.function.BiFunction;

class FetcherPermutations<ItemType extends Item> {

    @Inject
    private
    BiFunction<
            List<ItemType>,
            List<ItemType>,
            List<ItemType>> intersectionFetcher =
            new OrderedIntersectionFetcher<>();

    private
    FetcherUnidirectionalPermutations<ItemType> fetcherUnidirectionalPermutations =
            new FetcherUnidirectionalPermutations<>();

    private
    FetcherComplexPermutation<ItemType> fetcherComplexPermutation =
            new FetcherComplexPermutation<>();

    List<? extends Change<ItemType>> get(
            List<ItemType> sourceMiddle,
            List<ItemType> targetMiddle) {
        List<ItemType> intersectionSourceOrder =
                intersectionFetcher.apply(sourceMiddle, targetMiddle);
        List<ItemType> intersectionTargetOrder =
                intersectionFetcher.apply(targetMiddle, sourceMiddle);

        List<ChangeMove<ItemType>> moves = fetcherUnidirectionalPermutations.get(
                intersectionSourceOrder, intersectionTargetOrder, false);
        List<ChangeMove<ItemType>> movesReverse = fetcherUnidirectionalPermutations.get(
                intersectionSourceOrder, intersectionTargetOrder, true);

        if (moves == null && movesReverse == null) {

            return Collections.singletonList(
                    intersectionSourceOrder.isEmpty() || intersectionTargetOrder.isEmpty() ?
                            new ChangePersist<ItemType>() :
                            fetcherComplexPermutation.apply(
                                    intersectionSourceOrder,
                                    intersectionTargetOrder)
            );
        }

        if (moves == null) {
            return movesReverse;
        } else if (movesReverse == null) {
            return moves;
        }

        return movesReverse.size() < moves.size() ? movesReverse : moves;
    }
}
