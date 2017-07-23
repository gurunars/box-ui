package com.gurunars.crud_item_list

import org.junit.Test

import java.util.ArrayList
import java.util.Arrays

import junit.framework.Assert.assertTrue
import org.junit.Assert.assertFalse

class CheckerSolidChunkTest {

    @Test
    @Throws(Exception::class)
    fun emptySolidChunk_leadsToFalse() {
        assertFalse(isSolidChunk(ArrayList<Int>()))
    }

    @Test
    @Throws(Exception::class)
    fun largePositionDistance_leadsToFalse() {
        assertFalse(isSolidChunk(Arrays.asList(1, 2, 4)))
    }

    @Test
    @Throws(Exception::class)
    fun smallPositionDistance_leadsToTrue() {
        assertTrue(isSolidChunk(Arrays.asList(1, 2, 3)))
    }

}
