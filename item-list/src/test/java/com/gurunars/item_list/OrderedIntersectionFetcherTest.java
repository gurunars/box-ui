package com.gurunars.item_list;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class OrderedIntersectionFetcherTest {

    private OrderedIntersectionFetcher<Integer> fetcher = new OrderedIntersectionFetcher<>();

    @Test
    public void apply() throws Exception {
        assertEquals(Arrays.asList(
                2,3,5,6,7
        ), fetcher.apply(
                Arrays.asList(1,2,3,4,5,6,7,8),
                Arrays.asList(0,2,3,5,6,7,9,10)
        ));
    }

}