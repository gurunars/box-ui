package com.gurunars.item_list

import org.junit.Test

import java.util.Arrays

import org.junit.Assert.*

class OrderedDiffFetcherTest {

    private val fetcher = OrderedDiffFetcher<Int>()

    @Test
    @Throws(Exception::class)
    fun apply() {
        assertEquals(Arrays.asList(
                1, 4, 8
        ), fetcher.apply(
                Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8),
                Arrays.asList(2, 3, 5, 6, 7)
        ))
    }

}