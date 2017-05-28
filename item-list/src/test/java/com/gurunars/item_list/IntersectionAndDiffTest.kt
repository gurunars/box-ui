package com.gurunars.item_list

import org.junit.Test

import java.util.Arrays

import org.junit.Assert.assertEquals

class IntersectionAndDiffTest {

    @Test
    @Throws(Exception::class)
    fun orderedDiff_isCorrect() {
        assertEquals(
                Arrays.asList("one", "three", "five"),
                OrderedDiffFetcher<String>().apply(
                        Arrays.asList("one", "two", "three", "four", "five"),
                        Arrays.asList("two", "four", "seven", "eight")))
    }

    @Test
    @Throws(Exception::class)
    fun orderedIntersection_isCorrect() {
        assertEquals(
                Arrays.asList("two", "four"),
                OrderedIntersectionFetcher<String>().apply(
                        Arrays.asList("one", "two", "three", "four", "five"),
                        Arrays.asList("two", "four", "seven", "eight")))

    }

}
