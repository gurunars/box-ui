package com.gurunars.crud_item_list

import org.junit.Test
import org.mockito.internal.util.collections.Sets

import java.util.Arrays

import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue

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
