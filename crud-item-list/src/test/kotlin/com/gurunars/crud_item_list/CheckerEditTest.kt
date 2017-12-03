package com.gurunars.crud_item_list

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CheckerEditTest {

    private val checkerEdit = ActionEdit<StringItem>({})
    private val all = listOf("one", "two")

    @Test
    @Throws(Exception::class)
    fun selectionOfOne_leadsToTrue() {
        checkerEdit.canPerform(all, setOf("one"), { assertTrue(it) })
    }

    @Test
    @Throws(Exception::class)
    fun noSelection_leadsToFalse() {
        checkerEdit.canPerform(all, setOf(), { assertFalse(it) })
    }

    @Test
    @Throws(Exception::class)
    fun selectionOfMultipl_leadsToFalse() {
        checkerEdit.canPerform(all, setOf("one", "two"), { assertFalse(it) })
    }
}