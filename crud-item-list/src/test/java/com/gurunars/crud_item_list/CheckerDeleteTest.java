package com.gurunars.crud_item_list;

import org.junit.Test;
import org.mockito.internal.util.collections.Sets;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CheckerDeleteTest {

    private final ActionMoveDown<String> checkerDelete = new ActionMoveDown<>();
    private final List<String> all = Arrays.asList("one", "two");

    @Test
    public void selection_leadsToTrue() throws Exception {
        assertTrue(checkerDelete.canPerform(all, Sets.newSet("one")));
    }

    @Test
    public void noSelection_leadsToFalse() throws Exception {
        assertFalse(checkerDelete.canPerform(all, new HashSet<String>()));
    }

}
