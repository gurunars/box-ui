package com.gurunars.item_list

import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

import java.util.Arrays

import junit.framework.Assert.assertEquals
import org.mockito.Mockito.`when`

class ScrollPositionFetcherTest {

    private val fetcher = ScrollPositionFetcher()
    private val scroller = Mockito.mock(Scroller::class.java)
    private val items = Arrays.asList(
            AnimalItem(0, 0),
            AnimalItem(1, 0),
            AnimalItem(2, 0),
            AnimalItem(3, 0),
            AnimalItem(4, 0),
            AnimalItem(5, 0),
            AnimalItem(6, 0),
            AnimalItem(7, 0),
            AnimalItem(8, 0),
            AnimalItem(9, 0),
            AnimalItem(10, 0),
            AnimalItem(11, 0),
            AnimalItem(12, 0),
            AnimalItem(13, 0),
            AnimalItem(14, 0)
    )

    @Before
    fun setup() {
        `when`(scroller.findFirstVisibleItemPosition()).thenReturn(3) // upper bound @ 6
        `when`(scroller.findLastVisibleItemPosition()).thenReturn(12) // lower bound @ 9
    }

    @Test
    fun ifItemIsInVisibleRange_doNotScroll() {
        assertEquals(-1, fetcher.getScrollPosition(7, scroller, items))
    }

    @Test
    fun ifItemIsAboveTop_scrollThreeStepsAbovePosition() {
        assertEquals(2, fetcher.getScrollPosition(5, scroller, items))
    }

    @Test
    fun ifItemIsBelowBott_scrollThreeStepsBelowPosition() {
        assertEquals(13, fetcher.getScrollPosition(10, scroller, items))
    }

    @Test
    fun ifItemIsWayAboveTheTop_scollToZero() {
        assertEquals(0, fetcher.getScrollPosition(-1, scroller, items))
    }

    @Test
    fun ifItemIsWayBelowTheBottom_scollToBottom() {
        assertEquals(14, fetcher.getScrollPosition(50, scroller, items))
    }

}
