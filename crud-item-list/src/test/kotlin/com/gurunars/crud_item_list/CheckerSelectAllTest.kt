package com.gurunars.crud_item_list

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CheckerSelectAllTest {

    private val checkerSelectAll = ActionSelectAll<StringItem>()
    private val all = listOf("one", "two")

    @Test
    @Throws(Exception::class)
    fun noSelection_leadsToTrue() {
        checkerSelectAll.canPerform(all, HashSet<String>(), { assertTrue(it) })
    }

    @Test
    @Throws(Exception::class)
    fun selectionOfAll_leadsToFalse() {
        checkerSelectAll.canPerform(all, HashSet(all), { assertFalse(it) })
    }
}
