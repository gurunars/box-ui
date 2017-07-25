package com.gurunars.item_list.selectable_example

import android.support.test.InstrumentationRegistry.getInstrumentation
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.longClick
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.gurunars.test_utils.Helpers.nthChildOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class ActivityMainTest {

    @get:Rule
    var mActivityRule = ActivityTestRule(ActivityMain::class.java)

    private fun clickMenu(text: String) {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().targetContext)
        onView(withText(text)).perform(click())
    }

    private fun assertList(vararg expectedItems: String) {
        for (i in expectedItems.indices) {
            onView(nthChildOf(withId(R.id.recyclerView), i)).check(matches(withText(expectedItems[i])))
        }
    }

    private fun selectTwo() {
        onView(nthChildOf(withId(R.id.recyclerView), 0)).perform(longClick())
        onView(nthChildOf(withId(R.id.recyclerView), 1)).perform(click())
    }

    @Test
    fun longClickingOneAndClickingAnother_shouldSelectTwoItems() {
        selectTwo()
        assertList(
                "#0{TIGER @ 0}|true",
                "#1{WOLF @ 0}|true",
                "#2{MONKEY @ 0}|false",
                "#3{LION @ 0}|false"
        )
    }

    @Test
    fun deletingSelected_shouldRemoveTheItems() {
        selectTwo()
        clickMenu("Delete selected")
        assertList(
                "#2{MONKEY @ 0}|false",
                "#3{LION @ 0}|false"
        )
    }

    @Test
    fun updatingSelected_shouldIncrementCount() {
        selectTwo()
        clickMenu("Update selected")
        assertList(
                "#0{TIGER @ 1}|true",
                "#1{WOLF @ 1}|true",
                "#2{MONKEY @ 0}|false",
                "#3{LION @ 0}|false"
        )
    }

    @Test
    fun creatingItems_shouldNotUnselectItems() {
        selectTwo()
        clickMenu("Create items")
        assertList(
                "#0{TIGER @ 0}|true",
                "#1{WOLF @ 0}|true",
                "#2{MONKEY @ 0}|false",
                "#3{LION @ 0}|false",
                "#4{TIGER @ 0}|false",
                "#5{WOLF @ 0}|false",
                "#6{MONKEY @ 0}|false",
                "#7{LION @ 0}|false"
        )
    }

    @Test
    fun clickingClearSelection_shouldDropSelection() {
        selectTwo()
        clickMenu("Clear selection")
    }

    @Before
    fun before() {
        clickMenu("Reset items")
    }

    @After
    fun after() {
        clickMenu("Reset items")
    }

}