package com.gurunars.crud_item_list

import org.junit.Assert.assertEquals
import org.junit.Test

class MoverUpTest {

    private val moverUp = ActionMoveUp<StringItem>()

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
        "four",
        "five",
        "six",
        "seven",
        "three", // item moved down
        "eight",
        "nine",
        "ten"
    )

    @Test
    @Throws(Exception::class)
    fun moveUp_isCorrect() {
        moverUp.perform(all, selected, { all -> assertEquals(expectedOutcome, all) })
    }
}
