package com.gurunars.crud_item_list

import junit.framework.Assert.assertEquals
import org.junit.Test
import org.mockito.internal.util.collections.Sets
import java.util.*

class MoverDownTest {

    private val moverDown = ActionMoveDown<StringItem>()
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

    @Test
    @Throws(Exception::class)
    fun moveDown_isCorrect() {
        assertEquals(expectedOutcome, moverDown.perform(all, selected).first)
    }

}
