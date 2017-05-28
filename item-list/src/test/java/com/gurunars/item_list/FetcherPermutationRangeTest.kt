package com.gurunars.item_list

import org.junit.Test

import java.util.Arrays

import junit.framework.Assert.assertEquals

class FetcherPermutationRangeTest {

    private val fetcher = FetcherPermutationRange<Int>()

    private fun validate(source: List<Int>, target: List<Int>, expected: List<Int>) {
        val range = fetcher.apply(source, target)
        assertEquals(expected, target.subList(range.start, range.end))
    }

    @Test
    fun middleDiffFetching() {
        validate(
                Arrays.asList(1, 2, 3, 4, 5, 6),
                Arrays.asList(1, 5, 4, 3, 2, 6),
                Arrays.asList(5, 4, 3, 2))
    }

    @Test
    fun startDiffFetching() {
        validate(
                Arrays.asList(1, 2, 3, 4, 5, 6),
                Arrays.asList(3, 2, 1, 4, 5, 6),
                Arrays.asList(3, 2, 1))
    }

    @Test
    fun endDiffFetching() {
        validate(
                Arrays.asList(1, 2, 3, 4, 5, 6),
                Arrays.asList(1, 2, 3, 6, 5, 4),
                Arrays.asList(6, 5, 4))
    }

}
