package com.gurunars.item_list;

import java.util.Collections;
import java.util.List;

class FetcherPermutations<ItemType extends Item> {

    private
    FetcherUnidirectionalPermutations<ItemType> fetcherUnidirectionalPermutations =
            new FetcherUnidirectionalPermutations<>();

    private
    FetcherComplexPermutation<ItemType> fetcherComplexPermutation =
            new FetcherComplexPermutation<>();

    List<? extends Change<ItemType>> get(
            List<ItemType> intersectionSourceOrder,
            List<ItemType> intersectionTargetOrder) {

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
