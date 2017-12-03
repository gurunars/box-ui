package com.gurunars.crud_item_list

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CheckerDeleteTest {

    private val checkerDelete = ActionMoveDown<StringItem>()
    private val all = listOf("one", "two")

    @Test
    @Throws(Exception::class)
    fun selection_leadsToTrue() {
        checkerDelete.canPerform(all, setOf("one"), { assertTrue(it) })
    }

    @Test
    @Throws(Exception::class)
    fun noSelection_leadsToFalse() {
        checkerDelete.canPerform(all, setOf(), { assertFalse(it) })
    }
}
