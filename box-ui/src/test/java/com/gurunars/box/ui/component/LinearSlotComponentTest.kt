package com.gurunars.box.ui.component

import org.junit.Assert.*
import org.junit.Test

class LinearSlotComponentTest {

    val component = LinearSlotComponent(TextComponent())

    @Test
    fun checkSlot() {
        val diff = component.diff(
            component.empty,
            LinearSlot(
                key = 11,
                width = MatchParent,
                height = MatchParent,
                margin = Bounds(
                    left = 1.dp,
                    right = 2.dp,
                    top = 3.dp,
                    bottom = 4.dp
                ),
                child = Label(text="Not So Empty")
            )
        )
        assertEquals(
            listOf<Mutation>(),
            diff
        )
    }

}


