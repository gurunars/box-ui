package com.gurunars.android_utils.example

import android.app.Activity
import android.os.Bundle
import com.gurunars.box.box
import com.gurunars.box.patch
import com.gurunars.box.ui.component.*


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
                                    child=Label(
                                        text=i
                                    )
                                )
                            }
                        )
                    ),
                    LinearSlot(
                        width = MatchParent,
                        child = Label(
                            text = "Add more stuff ${children.size}",
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