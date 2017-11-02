package com.gurunars.crud_item_list

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.*

class CheckerSelectAllTest {

    private val checkerSelectAll = ActionSelectAll<StringItem>()
    private val all = listOf("one", "two")

    @Test
    @Throws(Exception::class)
    fun noSelection_leadsToTrue() {
        assertTrue(checkerSelectAll.canPerform(all, HashSet<String>()))
    }

    @Test
    @Throws(Exception::class)
    fun selectionOfAll_leadsToFalse() {
        assertFalse(checkerSelectAll.canPerform(all, HashSet(all)))
    }

}
