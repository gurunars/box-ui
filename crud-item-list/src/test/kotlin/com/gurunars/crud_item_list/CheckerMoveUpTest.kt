package com.gurunars.crud_item_list

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.internal.util.collections.Sets

import java.util.Arrays

class CheckerMoveUpTest {

    private val moveUpChecker = ActionMoveUp<StringItem>()
    private val all = Arrays.asList("one", "two", "three", "four")

    @Test
    @Throws(Exception::class)
    fun selectingFirstItem_leadsToFalse() {
        assertFalse(moveUpChecker.canPerform(all, Sets.newSet("one")))
    }

    @Test
    @Throws(Exception::class)
    fun selectingInterruptedChunk_leadsToFalse() {
        assertFalse(moveUpChecker.canPerform(all, Sets.newSet("two", "four")))
    }

    @Test
    @Throws(Exception::class)
    fun selectingSolidChunkBeforeLast_leadsToTrue() {
        assertTrue(moveUpChecker.canPerform(all, Sets.newSet("two", "three", "four")))
    }

}
