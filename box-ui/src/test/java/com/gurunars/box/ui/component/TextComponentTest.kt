package com.gurunars.box.ui.component

import android.widget.TextView
import com.nhaarman.mockito_kotlin.any
import org.junit.Test


class TextComponentTest {

    val component = TextComponent()

    @Test
    fun testDiffAgainstBlankValueAndClick() {
        component.diff(
            Label(), Label("Test", {})
        ).assertMutations<TextView> {
            text = "Test"
            setOnClickListener(any())
        }
    }

    @Test
    fun testDiffAgainstOtherValueAndClick() {
        component.diff(
            Label("One", {}), Label("Two", {})
        ).assertMutations<TextView> {
            text = "Two"
            setOnClickListener(any())
        }
    }

}