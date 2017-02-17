package com.gurunars.item_list;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class IntersectionAndDiffTest {

    @Test
    public void orderedDiff_isCorrect() throws Exception {
        assertEquals(
            Arrays.asList("one", "three", "five"),
            new OrderedDiffFetcher<String>().apply(
                Arrays.asList("one", "two", "three", "four", "five"),
                Arrays.asList("two", "four", "seven", "eight")));
    }

    @Test
    public void orderedIntersection_isCorrect() throws Exception {
        assertEquals(
                Arrays.asList("two", "four"),
                new OrderedIntersectionFetcher<String>().apply(
                        Arrays.asList("one", "two", "three", "four", "five"),
                        Arrays.asList("two", "four", "seven", "eight")));

    }

}
