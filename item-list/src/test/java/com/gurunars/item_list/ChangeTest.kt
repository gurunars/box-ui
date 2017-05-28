package com.gurunars.item_list

import android.support.v7.widget.RecyclerView
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.internal.verification.VerificationModeFactory.times
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import java.util.*

@RunWith(PowerMockRunner::class)
@PrepareForTest(RecyclerView.Adapter::class, ScrollPositionFetcher::class)
class ChangeTest {

    @Mock
    internal var adapter: RecyclerView.Adapter<*>? = null

    @Mock
    internal var scroller: Scroller? = null

    @Mock
    internal var scrollPositionFetcher: ScrollPositionFetcher? = null

    private val items = ArrayList<AnimalItem>()

    @Before
    fun setup() {
        items.clear()
    }

    private fun apply(change: Change<AnimalItem>): Int {
        return change.apply(adapter!!, scroller!!, items, -1)
    }

    @Test
    fun testChangeComplexPermutation() {
        items.addAll(Arrays.asList(
                AnimalItem(0, 0),
                AnimalItem(1, 0),
                AnimalItem(2, 0),
                AnimalItem(3, 0)
        ))
        val change = ChangeComplexPermutation(1,
                Arrays.asList(AnimalItem(2, 0), AnimalItem(3, 0), AnimalItem(1, 0)))
        assertEquals(-1, apply(change)) // TODO: verify via injection
        assertEquals(items, Arrays.asList(
                AnimalItem(0, 0),
                AnimalItem(2, 0),
                AnimalItem(3, 0),
                AnimalItem(1, 0)
        ))
        verify(adapter, times(1))?.notifyItemRangeChanged(1, 3)
    }

    @Test
    fun testChangeCreate() {
        val change = ChangeCreate(AnimalItem(0, 0), 0, 0)
        assertEquals(0, apply(change)) // TODO: verify via injection
        assertEquals(items, listOf(AnimalItem(0, 0)))
        verify(adapter, times(1))?.notifyItemInserted(0)
    }

    @Test
    fun testChangeDelete() {
        items.add(AnimalItem(0, 0))
        val change = ChangeDelete(AnimalItem(0, 0), 0, 0)
        assertEquals(-1, apply(change)) // TODO: verify via injection
        assertTrue(items.isEmpty())
        verify(adapter, times(1))?.notifyItemRemoved(0)
    }

    @Test
    fun testChangeMove() {
        items.addAll(Arrays.asList(AnimalItem(0, 0), AnimalItem(1, 1)))
        val change = ChangeMove(AnimalItem(0, 0), 0, 1)
        assertEquals(0, apply(change)) // TODO: verify via injection
        assertEquals(items, Arrays.asList(AnimalItem(1, 1), AnimalItem(0, 0)))
        verify(adapter, times(1))?.notifyItemMoved(0, 1)
    }

    @Test
    fun testChangeUpdate() {
        items.add(AnimalItem(0, 0))
        val change = ChangeUpdate(AnimalItem(0, 1), 0, 1)
        assertEquals(0, apply(change)) // TODO: verify via injection
        assertEquals(items, listOf(AnimalItem(0, 1)))
        verify(adapter, times(1))?.notifyItemChanged(0)
    }

    @Test
    fun testChangePersist() {
        val change = ChangePersist<AnimalItem>()
        assertEquals(-1, apply(change))
    }

}
