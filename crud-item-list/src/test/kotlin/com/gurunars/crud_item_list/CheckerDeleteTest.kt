package com.gurunars.crud_item_list

import org.junit.Test
import org.mockito.internal.util.collections.Sets

import java.util.Arrays
import java.util.HashSet

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue

class CheckerDeleteTest {

    private val checkerDelete = ActionMoveDown<StringItem>()
    private val all = Arrays.asList("one", "two")

    @Test
    @Throws(Exception::class)
    fun selection_leadsToTrue() {
        assertTrue(checkerDelete.canPerform(all, Sets.newSet("one")))
    }

    @Test
    @Throws(Exception::class)
    fun noSelection_leadsToFalse() {
        assertFalse(checkerDelete.canPerform(all, HashSet<String>()))
    }

}
