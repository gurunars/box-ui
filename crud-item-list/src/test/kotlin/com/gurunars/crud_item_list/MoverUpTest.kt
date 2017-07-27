package com.gurunars.crud_item_list

import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.internal.util.collections.Sets
import java.util.*

class MoverUpTest {

    private val moverUp = ActionMoveUp<StringItem>()

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

    @Test
    @Throws(Exception::class)
    fun moveUp_isCorrect() {
        assertEquals(expectedOutcome, moverUp.perform(all, selected).first)
    }

}
