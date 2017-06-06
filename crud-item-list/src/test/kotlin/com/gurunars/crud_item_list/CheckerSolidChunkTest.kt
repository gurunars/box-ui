package com.gurunars.crud_item_list

import org.junit.Test

import java.util.ArrayList
import java.util.Arrays

import junit.framework.Assert.assertTrue
import org.junit.Assert.assertFalse

class CheckerSolidChunkTest {

    private val checkerSolidChunk = CheckerSolidChunk()

    @Test
    @Throws(Exception::class)
    fun emptySolidChunk_leadsToFalse() {
        assertFalse(checkerSolidChunk.apply(ArrayList<Int>()))
    }

    @Test
    @Throws(Exception::class)
    fun largePositionDistance_leadsToFalse() {
        assertFalse(checkerSolidChunk.apply(Arrays.asList(1, 2, 4)))
    }

    @Test
    @Throws(Exception::class)
    fun smallPositionDistance_leadsToTrue() {
        assertTrue(checkerSolidChunk.apply(Arrays.asList(1, 2, 3)))
    }

}
