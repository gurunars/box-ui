package com.gurunars.crud_item_list

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test


class CheckerMoveDownTest {

    private val moveDownChecker = ActionMoveDown<StringItem>()
    private val all = listOf("one", "two", "three", "four")

    @Test
    @Throws(Exception::class)
    fun selectingLastItem_leadsToFalse() {
        moveDownChecker.canPerform(all, setOf("four"), { assertFalse(it) })
    }

    @Test
    @Throws(Exception::class)
    fun selectingInterruptedChunk_leadsToFalse() {
        moveDownChecker.canPerform(all, setOf("one", "three"), { assertFalse(it) })
    }

    @Test
    @Throws(Exception::class)
    fun selectingSolidChunkBeforeLast_leadsToTrue() {
        moveDownChecker.canPerform(all, setOf("one", "two", "three"), { assertTrue(it) })
    }

}
