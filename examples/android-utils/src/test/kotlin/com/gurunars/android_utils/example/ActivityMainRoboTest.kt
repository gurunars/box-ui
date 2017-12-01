package com.gurunars.android_utils.example

import android.app.Activity
import android.view.View
import android.widget.TextView
import org.jetbrains.anko.find
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config


@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class ActivityMainRoboTest {
    private lateinit var activity: Activity

    @Before
    fun setUp() {
        activity = Robolectric.setupActivity(ActivityMain::class.java)
    }

    private fun validateText(text: String) {
        assertEquals(
            text,
            activity.find<TextView>(R.id.payloadView).text.toString()
        )
    }

    private fun click(id: Int) {
        activity.find<View>(id).performClick()
    }

    @Test
    fun clickingButtons_shouldNotProduceExceptions() {
        click(R.id.clear)
        click(R.id.disabled)
        validateText("Empty")
        click(R.id.set)
        validateText("Configured")
        click(R.id.clear)
        validateText("Empty")
    }

}