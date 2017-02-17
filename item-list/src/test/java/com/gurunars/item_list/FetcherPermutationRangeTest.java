package com.gurunars.item_list;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;

public class FetcherPermutationRangeTest {

    private FetcherPermutationRange<Integer> fetcher = new FetcherPermutationRange<>();

    private void validate(List<Integer> source, List<Integer> target, List<Integer> expected) {
        FetcherPermutationRange.Range range = fetcher.apply(source, target);
        assertEquals(expected, target.subList(range.getStart(), range.getEnd()));
    }

    @Test
    public void middleDiffFetching() {
        validate(
                Arrays.asList(1,2,3,4,5,6),
                Arrays.asList(1,5,4,3,2,6),
                Arrays.asList(5,4,3,2));
    }

    @Test
    public void startDiffFetching() {
        validate(
                Arrays.asList(1,2,3,4,5,6),
                Arrays.asList(3,2,1,4,5,6),
                Arrays.asList(3,2,1));
    }

    @Test
    public void endDiffFetching() {
        validate(
                Arrays.asList(1,2,3,4,5,6),
                Arrays.asList(1,2,3,6,5,4),
                Arrays.asList(6,5,4));
    }

}
