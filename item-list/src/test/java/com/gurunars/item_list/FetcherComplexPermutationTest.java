package com.gurunars.item_list;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class FetcherComplexPermutationTest {

    private FetcherComplexPermutation<Item<Integer>> fetcher = new FetcherComplexPermutation<>();

    @Test
    public void getNormal() {
        assertEquals(fetcher.apply(
                Arrays.asList(
                        new Item<>(1, 1),
                        new Item<>(2, 1),
                        new Item<>(3, 1),
                        new Item<>(4, 1)
                ),
                Arrays.asList(
                        new Item<>(1, 1),
                        new Item<>(3, 1),
                        new Item<>(2, 1),
                        new Item<>(4, 1)
                )),
                new ChangeComplexPermutation<>(1, Arrays.asList(
                        new Item<>(3, 1),
                        new Item<>(2, 1)
                ))
        );
    }
}