package com.gurunars.crud_item_list;

import org.junit.Test;
import org.mockito.internal.util.collections.Sets;

import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class CheckerMoveDownTest {

    private final ActionMoveDown<String> moveDownChecker = new ActionMoveDown<>();
    private final List<String> all = Arrays.asList("one", "two", "three", "four");

    @Test
    public void selectingLastItem_leadsToFalse() throws Exception {
        assertFalse(moveDownChecker.canPerform(all, Sets.newSet("four")));
    }

    @Test
    public void selectingInterruptedChunk_leadsToFalse() throws Exception {
        assertFalse(moveDownChecker.canPerform(all, Sets.newSet("one", "three")));
    }

    @Test
    public void selectingSolidChunkBeforeLast_leadsToTrue() throws Exception {
        assertTrue(moveDownChecker.canPerform(all, Sets.newSet("one", "two", "three")));
    }

}
