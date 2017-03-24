package com.gurunars.item_list;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class FetcherComplexPermutationTest {

    private FetcherComplexPermutation<AnimalItem> fetcher = new FetcherComplexPermutation<>();

    @Test
    public void getNormal() {
        assertEquals(fetcher.apply(
                Arrays.asList(
                        new AnimalItem(1, 1),
                        new AnimalItem(2, 1),
                        new AnimalItem(3, 1),
                        new AnimalItem(4, 1)
                ),
                Arrays.asList(
                        new AnimalItem(1, 1),
                        new AnimalItem(3, 1),
                        new AnimalItem(2, 1),
                        new AnimalItem(4, 1)
                )),
                new ChangeComplexPermutation<>(1, Arrays.asList(
                        new AnimalItem(3, 1),
                        new AnimalItem(2, 1)
                ))
        );
    }
}