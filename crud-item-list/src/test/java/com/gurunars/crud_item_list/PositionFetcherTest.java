package com.gurunars.crud_item_list;

import org.junit.Test;
import org.mockito.internal.util.collections.Sets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static junit.framework.Assert.assertEquals;

public class PositionFetcherTest {

    private final PositionFetcher<String> positionFetcher = new PositionFetcher<>();
    private final List<String> all = Arrays.asList("one", "two", "three", "four");
    private final Set<String> selected = Sets.newSet("two", "three");

    @Test
    public void emptyAll_leadToNoPositions() throws Exception {
        assertEquals(positionFetcher.apply(new ArrayList<String>(), selected), new ArrayList<>());
    }

    @Test
    public void emptySelections_leadToNoPositions() throws Exception {
        assertEquals(positionFetcher.apply(all, new HashSet<String>()), new ArrayList<>());
    }

    @Test
    public void properSelections_leadProperPositions() throws Exception {
        assertEquals(Arrays.asList(1, 2), positionFetcher.apply(all, selected));
    }

}
