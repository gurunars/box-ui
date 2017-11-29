package com.gurunars.crud_item_list

import org.junit.Assert.assertEquals
import org.junit.Test

class MoverDownTest {

    private val moverDown = ActionMoveDown<StringItem>()
    private val all = listOf(
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
    private val selected = setOf(
        "four",
        "five",
        "six",
        "seven"
    )

    private val expectedOutcome = listOf(
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
        moverDown.perform(all, selected, { all -> assertEquals(expectedOutcome, all) })
    }

}
