package com.gurunars.crud_item_list

import org.junit.Test
import org.mockito.internal.util.collections.Sets

import java.util.Arrays
import java.util.HashSet

import java8.util.function.Consumer

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue

class CheckerEditTest {

    private val checkerEdit = ActionEdit(Consumer<String> { })
    private val all = Arrays.asList("one", "two")

    @Test
    @Throws(Exception::class)
    fun selectionOfOne_leadsToTrue() {
        assertTrue(checkerEdit.canPerform(all, Sets.newSet("one")))
    }

    @Test
    @Throws(Exception::class)
    fun noSelection_leadsToFalse() {
        assertFalse(checkerEdit.canPerform(all, HashSet<String>()))
    }

    @Test
    @Throws(Exception::class)
    fun selectionOfMultipl_leadsToFalse() {
        assertFalse(checkerEdit.canPerform(all, Sets.newSet("one", "two")))
    }

}
