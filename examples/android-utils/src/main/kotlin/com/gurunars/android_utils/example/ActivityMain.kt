package com.gurunars.android_utils.example

import android.app.Activity
import android.os.Bundle
import com.gurunars.box.box
import com.gurunars.box.patch
import com.gurunars.box.ui.functional.*


data class SampleState(
    val compact: Boolean = false,
    val children: List<String> = listOf("0")
)


class ActivityMain : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val state = SampleState().box

        ui(state) {
            LinearContainer(
                orientation = LinearContainer.Orientation.VERTICAL,
                children = listOf(
                    LinearSlot(
                        child = LinearContainer(
                            orientation = LinearContainer.Orientation.VERTICAL,
                            children = children.mapIndexed { index: Int, i: String ->
                                LinearSlot(
                                    width = MatchParent,
                                    key=index,
                                    child=Text(
                                        value=i
                                    )
                                )
                            }
                        )
                    ),
                    LinearSlot(
                        width = MatchParent,
                        child = Text(
                            value = "Add more stuff ${children.size}",
                            onClick = {
                                state.patch {
                                    copy(
                                        children = children + children.size.toString()
                                    )
                                }
                            }
                        )
                    )
                )
            )
        }
    }
}