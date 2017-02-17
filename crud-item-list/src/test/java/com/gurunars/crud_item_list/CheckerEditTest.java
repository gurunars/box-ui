package com.gurunars.crud_item_list;

import org.junit.Test;
import org.mockito.internal.util.collections.Sets;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CheckerEditTest {

    private final CheckerEdit<String> checkerEdit = new CheckerEdit<>();
    private final List<String> all = Arrays.asList("one", "two");

    @Test
    public void selectionOfOne_leadsToTrue() throws Exception {
        assertTrue(checkerEdit.apply(all, Sets.newSet("one")));
    }

    @Test
    public void noSelection_leadsToFalse() throws Exception {
        assertFalse(checkerEdit.apply(all, new HashSet<String>()));
    }

    @Test
    public void selectionOfMultipl_leadsToFalse() throws Exception {
        assertFalse(checkerEdit.apply(all, Sets.newSet("one", "two")));
    }

}
