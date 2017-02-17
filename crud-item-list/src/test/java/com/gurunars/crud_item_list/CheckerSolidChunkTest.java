package com.gurunars.crud_item_list;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class CheckerSolidChunkTest {

    private final CheckerSolidChunk checkerSolidChunk = new CheckerSolidChunk();

    @Test
    public void emptySolidChunk_leadsToFalse() throws Exception {
        assertFalse(checkerSolidChunk.apply(new ArrayList<Integer>()));
    }

    @Test
    public void largePositionDistance_leadsToFalse() throws Exception {
        assertFalse(checkerSolidChunk.apply(Arrays.asList(1, 2, 4)));
    }

    @Test
    public void smallPositionDistance_leadsToTrue() throws Exception {
        assertTrue(checkerSolidChunk.apply(Arrays.asList(1, 2, 3)));
    }

}
