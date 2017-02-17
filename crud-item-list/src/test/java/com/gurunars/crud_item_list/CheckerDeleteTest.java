package com.gurunars.crud_item_list;

import org.junit.Test;
import org.mockito.internal.util.collections.Sets;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CheckerDeleteTest {

    private final CheckerDelete<String> checkerDelete = new CheckerDelete<>();
    private final List<String> all = Arrays.asList("one", "two");

    @Test
    public void selection_leadsToTrue() throws Exception {
        assertTrue(checkerDelete.apply(all, Sets.newSet("one")));
    }

    @Test
    public void noSelection_leadsToFalse() throws Exception {
        assertFalse(checkerDelete.apply(all, new HashSet<String>()));
    }

}
