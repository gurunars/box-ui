package com.gurunars.crud_item_list

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CheckerSolidChunkTest {

    @Test
    @Throws(Exception::class)
    fun emptySolidChunk_leadsToFalse() {
        assertFalse(isSolidChunk(listOf()))
    }

    @Test
    @Throws(Exception::class)
    fun largePositionDistance_leadsToFalse() {
        assertFalse(isSolidChunk(listOf(1, 2, 4)))
    }

    @Test
    @Throws(Exception::class)
    fun smallPositionDistance_leadsToTrue() {
        assertTrue(isSolidChunk(listOf(1, 2, 3)))
    }
}
