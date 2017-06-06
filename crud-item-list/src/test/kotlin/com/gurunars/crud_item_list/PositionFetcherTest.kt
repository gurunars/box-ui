package com.gurunars.crud_item_list

import org.junit.Test
import org.mockito.internal.util.collections.Sets

import java.util.ArrayList
import java.util.Arrays
import java.util.HashSet

import junit.framework.Assert.assertEquals

class PositionFetcherTest {

    private val positionFetcher = PositionFetcher<String>()
    private val all = Arrays.asList("one", "two", "three", "four")
    private val selected = Sets.newSet("two", "three")

    @Test
    @Throws(Exception::class)
    fun emptyAll_leadToNoPositions() {
        assertEquals(positionFetcher.apply(ArrayList<String>(), selected), ArrayList<Any>())
    }

    @Test
    @Throws(Exception::class)
    fun emptySelections_leadToNoPositions() {
        assertEquals(positionFetcher.apply(all, HashSet<String>()), ArrayList<Any>())
    }

    @Test
    @Throws(Exception::class)
    fun properSelections_leadProperPositions() {
        assertEquals(Arrays.asList(1, 2), positionFetcher.apply(all, selected))
    }

}
