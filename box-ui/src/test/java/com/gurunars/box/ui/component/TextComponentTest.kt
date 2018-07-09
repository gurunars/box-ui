package com.gurunars.box.ui.component

import android.widget.TextView
import com.gurunars.box.ui.component.text.Text
import com.gurunars.box.ui.component.text.TextComponent
import com.nhaarman.mockito_kotlin.any
import org.junit.Test


class TextComponentTest {

    val component = TextComponent()

    @Test
    fun testDiffAgainstBlankValueAndClick() {
        component.diff(
            Text(),
            Text("Test", {})
        ).assertMutations<TextView> {
            text = "Test"
            setOnClickListener(any())
        }
    }

    @Test
    fun testDiffAgainstOtherValueAndClick() {
        component.diff(
            Text("One", {}),
            Text("Two", {})
        ).assertMutations<TextView> {
            text = "Two"
            setOnClickListener(any())
        }
    }

}