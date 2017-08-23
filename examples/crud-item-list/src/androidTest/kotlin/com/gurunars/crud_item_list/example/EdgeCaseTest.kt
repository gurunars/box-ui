package com.gurunars.crud_item_list.example

import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import android.support.test.InstrumentationRegistry.getInstrumentation
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText

@RunWith(AndroidJUnit4::class)
@LargeTest
class EdgeCaseTest {

    @get:Rule
    var mActivityRule = ActivityTestRule(
            ActivityMain::class.java)

    // TODO: TEST continuous press of the move buttons
    // TODO: TEST move down several till the almost bottom (item should mot be near the edge)
    // TODO: TEST move up one item till the almost top (item should not be near the edge)
    // TODO: TEST move down one item till the almost bottom (item should mot be near the edge)
    // TODO: TEST scroll till footer
    // TODO: TEST no scroll when updating/creating if in visibility range
    // TODO: TEST scroll when updating/creating if not in visibility range
    // TODO: TEST scrolling top from below the bottom - item should get visibility right away
    // TODO: TEST scrolling bottom from above the top - item should get visibility right away

    private fun init() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().targetContext)
        onView(withText("Add many")).perform(click())
        onView(withId(R.id.unlock)).perform(click())
    }

    @Test
    fun toDo() {
    }

    @Before
    fun before() {
        init()
    }

    @After
    fun after() {
        init()
    }

}