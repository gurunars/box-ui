package com.gurunars.crud_item_list

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.internal.util.collections.Sets
import java.util.*

class CheckerMoveUpTest {

    private val moveUpChecker = ActionMoveUp<StringItem>()
    private val all = listOf("one", "two", "three", "four")

    @Test
    @Throws(Exception::class)
    fun selectingFirstItem_leadsToFalse() {
        moveUpChecker.canPerform(all, setOf("one"), {assertFalse(it)})
    }

    @Test
    @Throws(Exception::class)
    fun selectingInterruptedChunk_leadsToFalse() {
        moveUpChecker.canPerform(all, setOf("two", "four"), {assertFalse(it)})
    }

    @Test
    @Throws(Exception::class)
    fun selectingSolidChunkBeforeLast_leadsToTrue() {
        moveUpChecker.canPerform(all, setOf("two", "three", "four"), {assertTrue(it)})
    }

}
