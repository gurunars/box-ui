package com.gurunars.crud_item_list

import org.junit.Test

import java.util.Arrays
import java.util.HashSet

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue

class CheckerSelectAllTest {

    private val checkerSelectAll = ActionSelectAll<String>()
    private val all = Arrays.asList("one", "two")

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
