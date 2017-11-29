package com.gurunars.crud_item_list

import org.junit.Assert.assertEquals
import org.junit.Test


fun getRawPositions(items: List<String>, selectedItems: Set<String>): List<Int> =
    getPositions(items.itemize(), selectedItems.itemize())

class PositionFetcherTest {

    private val all = listOf("one", "two", "three", "four")
    private val selected = setOf("two", "three")

    @Test
    @Throws(Exception::class)
    fun emptyAll_leadToNoPositions() {
        assertEquals(getRawPositions(listOf(), selected), listOf<Any>())
    }

    @Test
    @Throws(Exception::class)
    fun emptySelections_leadToNoPositions() {
        assertEquals(getRawPositions(all, hashSetOf()), listOf<Any>())
    }

    @Test
    @Throws(Exception::class)
    fun properSelections_leadProperPositions() {
        assertEquals(listOf(1, 2), getRawPositions(all, selected))
    }

}
