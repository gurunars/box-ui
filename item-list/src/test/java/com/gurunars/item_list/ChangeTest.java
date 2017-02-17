package com.gurunars.item_list;

import android.support.v7.widget.RecyclerView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(PowerMockRunner.class)
@PrepareForTest({RecyclerView.Adapter.class})
public class ChangeTest {

    @Mock
    RecyclerView.Adapter adapter;

    @Mock
    Scroller scroller;

    @Mock
    ScrollPositionFetcher scrollPositionFetcher;

    private List<TestItem> items = new ArrayList<>();

    @Before
    public void setup() {
        items.clear();
    }

    private int apply(Change<TestItem> change) {
        return change.apply(adapter, scroller, items, -1);
    }

    @Test
    public void testChangeComplexPermutation() {
        items.addAll(Arrays.asList(
                new TestItem(0, 0),
                new TestItem(1, 0),
                new TestItem(2, 0),
                new TestItem(3, 0)
        ));
        Change<TestItem> change = new ChangeComplexPermutation<>(1,
                Arrays.asList(new TestItem(2, 0), new TestItem(3, 0), new TestItem(1, 0)));
        assertEquals(-1, apply(change)); // TODO: verify via injection
        assertEquals(items, Arrays.asList(
                new TestItem(0, 0),
                new TestItem(2, 0),
                new TestItem(3, 0),
                new TestItem(1, 0)
        ));
        verify(adapter, times(1)).notifyItemRangeChanged(1, 3);
    }

    @Test
    public void testChangeCreate() {
        Change<TestItem> change = new ChangeCreate<>(new TestItem(0, 0), 0, 0);
        assertEquals(0, apply(change)); // TODO: verify via injection
        assertEquals(items, Collections.singletonList(new TestItem(0, 0)));
        verify(adapter, times(1)).notifyItemInserted(0);
    }

    @Test
    public void testChangeDelete() {
        items.add(new TestItem(0, 0));
        Change<TestItem> change = new ChangeDelete<>(new TestItem(0, 0), 0, 0);
        assertEquals(-1, apply(change)); // TODO: verify via injection
        assertTrue(items.isEmpty());
        verify(adapter, times(1)).notifyItemRemoved(0);
    }

    @Test
    public void testChangeMove() {
        items.addAll(Arrays.asList(new TestItem(0, 0), new TestItem(1, 1)));
        Change<TestItem> change = new ChangeMove<>(new TestItem(0, 0), 0, 1);
        assertEquals(0, apply(change)); // TODO: verify via injection
        assertEquals(items, Arrays.asList(new TestItem(1, 1), new TestItem(0, 0)));
        verify(adapter, times(1)).notifyItemMoved(0, 1);
    }

    @Test
    public void testChangeUpdate() {
        items.add(new TestItem(0, 0));
        Change<TestItem> change = new ChangeUpdate<>(new TestItem(0, 1), 0, 1);
        assertEquals(0, apply(change)); // TODO: verify via injection
        assertEquals(items, Collections.singletonList(new TestItem(0, 1)));
        verify(adapter, times(1)).notifyItemChanged(0);
    }

    @Test
    public void testChangePersist() {
        Change<TestItem> change = new ChangePersist<>();
        assertEquals(-1, apply(change));
    }

}
