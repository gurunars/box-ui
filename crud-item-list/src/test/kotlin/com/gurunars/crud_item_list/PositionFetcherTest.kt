package com.gurunars.crud_item_list

import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.internal.util.collections.Sets

import java.util.ArrayList
import java.util.Arrays
import java.util.HashSet


fun getPositions(items: List<String>, selectedItems: Set<String>) =
    getPositions(items.itemize(), selectedItems.itemize())

class PositionFetcherTest {

    private val all = Arrays.asList("one", "two", "three", "four")
    private val selected = Sets.newSet("two", "three")

    @Test
    @Throws(Exception::class)
    fun emptyAll_leadToNoPositions() {
        assertEquals(getPositions(ArrayList<String>(), selected), ArrayList<Any>())
    }

    @Test
    @Throws(Exception::class)
    fun emptySelections_leadToNoPositions() {
        assertEquals(getPositions(all, HashSet<String>()), ArrayList<Any>())
    }

    @Test
    @Throws(Exception::class)
    fun properSelections_leadProperPositions() {
        assertEquals(Arrays.asList(1, 2), getPositions(all, selected))
    }

}
