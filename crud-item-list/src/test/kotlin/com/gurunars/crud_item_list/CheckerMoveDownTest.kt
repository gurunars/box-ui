package com.gurunars.crud_item_list

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.internal.util.collections.Sets
import java.util.*


class CheckerMoveDownTest {

    private val moveDownChecker = ActionMoveDown<StringItem>()
    private val all = Arrays.asList("one", "two", "three", "four")

    @Test
    @Throws(Exception::class)
    fun selectingLastItem_leadsToFalse() {
        assertFalse(moveDownChecker.canPerform(all, Sets.newSet("four")))
    }

    @Test
    @Throws(Exception::class)
    fun selectingInterruptedChunk_leadsToFalse() {
        assertFalse(moveDownChecker.canPerform(all, Sets.newSet("one", "three")))
    }

    @Test
    @Throws(Exception::class)
    fun selectingSolidChunkBeforeLast_leadsToTrue() {
        assertTrue(moveDownChecker.canPerform(all, Sets.newSet("one", "two", "three")))
    }

}
