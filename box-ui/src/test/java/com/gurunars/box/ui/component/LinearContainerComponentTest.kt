package com.gurunars.box.ui.component

import android.widget.LinearLayout
import org.junit.Test

class LinearContainerComponentTest {

    val component = LinearContainerComponent()

    @Test
    fun checkContainer() {
        component.diff(
            component.empty,
            LinearContainer(
                orientation = LinearContainer.Orientation.VERTICAL,
                children = listOf(
                    LinearSlot(
                        child = Label(text = "Not So Empty")
                    )
                )
            )
        ).assertMutations<LinearLayout> {
            orientation = LinearLayout.VERTICAL
            // TODO: assertMutations the children
        }
    }


}