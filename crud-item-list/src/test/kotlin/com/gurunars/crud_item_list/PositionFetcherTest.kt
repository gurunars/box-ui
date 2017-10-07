package com.gurunars.crud_item_list

import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.internal.util.collections.Sets
import java.util.*


fun getRawPositions(items: List<String>, selectedItems: Set<String>): List<Int> =
    getPositions(items.itemize(), selectedItems.itemize())

class PositionFetcherTest {

    private val all = Arrays.asList("one", "two", "three", "four")
    private val selected = Sets.newSet("two", "three")

    @Test
    @Throws(Exception::class)
    fun emptyAll_leadToNoPositions() {
        assertEquals(getRawPositions(ArrayList<String>(), selected), ArrayList<Any>())
    }

    @Test
    @Throws(Exception::class)
    fun emptySelections_leadToNoPositions() {
        assertEquals(getRawPositions(all, HashSet<String>()), ArrayList<Any>())
    }

    @Test
    @Throws(Exception::class)
    fun properSelections_leadProperPositions() {
        assertEquals(Arrays.asList(1, 2), getRawPositions(all, selected))
    }

}
