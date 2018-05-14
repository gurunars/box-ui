package com.gurunars.crud_item_list

import org.junit.Assert.*
import org.junit.Test

class ProcessItemInEditTest {

    val source = listOf("One", "Two", "Three").itemize()

    @Test
    fun addingNullValue_doesNothing() {
        assertEquals(
            processItemInEdit(true, source, null),
            source
        )
    }

    @Test
    fun addingToTail_appendsToTail() {
        assertEquals(
            processItemInEdit(true, source, StringItem("Four")),
            source + StringItem("Four")
        )
    }

    @Test
    fun addingToHead_prependsToHead() {
        assertEquals(
            processItemInEdit(false, source, StringItem("Four")),
            listOf(StringItem("Four")) + source
        )
    }

    @Test
    fun updatingItem_changesValue() {
        assertEquals(
            processItemInEdit(false, source, source[0].copy(text="OneDelta")),
            listOf(source[0].copy(text="OneDelta")) + listOf(source[1], source[2])
        )
    }

}