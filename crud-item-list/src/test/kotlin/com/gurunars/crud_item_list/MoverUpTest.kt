package com.gurunars.crud_item_list

import org.junit.Test
import org.mockito.internal.util.collections.Sets

import java.util.ArrayList
import java.util.Arrays

import junit.framework.Assert.assertEquals

class MoverUpTest {

    private val moverUp = ActionMoveUp<String>()

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
            "four",
            "five",
            "six",
            "seven",
            "three", // item moved down
            "eight",
            "nine",
            "ten"
    )

    private fun apply(selected: Set<String>): List<String> {
        val result = ArrayList(all)
        moverUp.perform(result, selected)
        return result
    }

    @Test
    @Throws(Exception::class)
    fun moveUp_isCorrect() {
        assertEquals(expectedOutcome, apply(selected))
    }

}
