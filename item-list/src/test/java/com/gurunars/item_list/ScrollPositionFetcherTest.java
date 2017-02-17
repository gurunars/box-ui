package com.gurunars.item_list;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class ScrollPositionFetcherTest {

    private ScrollPositionFetcher fetcher = new ScrollPositionFetcher();
    private Scroller scroller = Mockito.mock(Scroller.class);
    private List<TestItem> items = Arrays.asList(
            new TestItem(0, 0),
            new TestItem(1, 0),
            new TestItem(2, 0),
            new TestItem(3, 0),
            new TestItem(4, 0),
            new TestItem(5, 0),
            new TestItem(6, 0),
            new TestItem(7, 0),
            new TestItem(8, 0),
            new TestItem(9, 0),
            new TestItem(10, 0),
            new TestItem(11, 0),
            new TestItem(12, 0),
            new TestItem(13, 0),
            new TestItem(14, 0)
    );

    @Before
    public void setup() {
        when(scroller.findFirstVisibleItemPosition()).thenReturn(3); // upper bound @ 6
        when(scroller.findLastVisibleItemPosition()).thenReturn(12); // lower bound @ 9
    }

    @Test
    public void ifItemIsInVisibleRange_doNotScroll() {
        assertEquals(-1, fetcher.getScrollPosition(7, scroller, items));
    }

    @Test
    public void ifItemIsAboveTop_scrollThreeStepsAbovePosition() {
        assertEquals(2, fetcher.getScrollPosition(5, scroller, items));
    }

    @Test
    public void ifItemIsBelowBott_scrollThreeStepsBelowPosition() {
        assertEquals(13, fetcher.getScrollPosition(10, scroller, items));
    }

    @Test
    public void ifItemIsWayAboveTheTop_scollToZero() {
        assertEquals(0, fetcher.getScrollPosition(-1, scroller, items));
    }

    @Test
    public void ifItemIsWayBelowTheBottom_scollToBottom() {
        assertEquals(14, fetcher.getScrollPosition(50, scroller, items));
    }

}
