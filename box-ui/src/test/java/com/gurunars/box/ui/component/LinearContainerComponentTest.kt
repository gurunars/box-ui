package com.gurunars.box.ui.component

import android.widget.LinearLayout
import com.gurunars.box.ui.component.text.Text
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
                        child = Text(value = "Not So Empty")
                    )
                )
            )
        ).assertMutations<LinearLayout> {
            orientation = LinearLayout.VERTICAL
            // TODO: assertMutations the children
        }
    }


}