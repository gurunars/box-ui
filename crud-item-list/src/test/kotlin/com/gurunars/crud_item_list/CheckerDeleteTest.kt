package com.gurunars.crud_item_list

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.internal.util.collections.Sets
import java.util.*

class CheckerDeleteTest {

    private val checkerDelete = ActionMoveDown<StringItem>()
    private val all = listOf("one", "two")

    @Test
    @Throws(Exception::class)
    fun selection_leadsToTrue() {
        assertTrue(checkerDelete.canPerform(all, setOf("one")))
    }

    @Test
    @Throws(Exception::class)
    fun noSelection_leadsToFalse() {
        assertFalse(checkerDelete.canPerform(all, setOf()))
    }

}
