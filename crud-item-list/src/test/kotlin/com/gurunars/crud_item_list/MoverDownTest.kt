package com.gurunars.crud_item_list

import org.junit.Test
import org.mockito.internal.util.collections.Sets

import java.util.ArrayList
import java.util.Arrays

import junit.framework.Assert.assertEquals

class MoverDownTest {

    private val moverDown = ActionMoveDown<String>()
    private val all = Arrays.asList(
            "one",
            "two",
            "three",
            "four",
            "five",
            "six",
            "seven",
            "eight",
            "nine",
            "ten"
    )
    private val selected = Sets.newSet(
            "four",
            "five",
            "six",
            "seven"
    )

    private val expectedOutcome = Arrays.asList(
            "one",
            "two",
            "three",
            "eight", // item moved up
            "four",
            "five",
            "six",
            "seven",
            "nine",
            "ten"
    )

    private fun apply(selected: Set<String>): List<String> {
        val result = ArrayList(all)
        moverDown.perform(result, selected)
        return result
    }

    @Test
    @Throws(Exception::class)
    fun moveDown_isCorrect() {
        assertEquals(expectedOutcome, apply(selected))
    }

}
