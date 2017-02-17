package com.gurunars.item_list;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class FetcherComplexPermutationTest {

    private FetcherComplexPermutation<TestItem> fetcher = new FetcherComplexPermutation<>();

    @Test
    public void getNormal() {
        assertEquals(fetcher.apply(
                ItemHolder.wrap(Arrays.asList(
                        new TestItem(1, 1),
                        new TestItem(2, 1),
                        new TestItem(3, 1),
                        new TestItem(4, 1)
                )),
                ItemHolder.wrap(Arrays.asList(
                        new TestItem(1, 1),
                        new TestItem(3, 1),
                        new TestItem(2, 1),
                        new TestItem(4, 1)
                ))),
                new ChangeComplexPermutation<>(1, Arrays.asList(
                        new TestItem(3, 1),
                        new TestItem(2, 1)
                ))
        );
    }
}