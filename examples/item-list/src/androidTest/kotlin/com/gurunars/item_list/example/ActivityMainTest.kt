package com.gurunars.item_list.example

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
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import com.gurunars.test_utils.Helpers.nthChildOf

@RunWith(AndroidJUnit4::class)
@LargeTest
class ActivityMainTest {

    @Rule
    var mActivityRule = ActivityTestRule(
            ActivityMain::class.java)

    private fun clickMenu(text: String) {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().targetContext)
        onView(withText(text)).perform(click())
    }

    @get:Rule
    fun clickingClear_shouldShowEmptyListView() {
        clickMenu("Clear")
        onView(withText("Empty")).check(matches(isDisplayed()))
    }

    private fun assertList(vararg expectedItems: String) {
        for (i in expectedItems.indices) {
            onView(nthChildOf(withId(R.id.recyclerView), i)).check(matches(withText(expectedItems[i])))
        }
    }

    @Test
    fun deletingItems_shouldLeadToPartialRemoval() {
        clickMenu("Delete items")
        assertList(
                "#0{TIGER @ 0}",
                "#2{MONKEY @ 0}"
        )
    }

    @Test
    fun createItems_shouldAppendItemsToTheEnd() {
        clickMenu("Create items")
        assertList(
                "#0{TIGER @ 0}",
                "#1{WOLF @ 0}",
                "#2{MONKEY @ 0}",
                "#3{LION @ 0}",
                "#4{TIGER @ 0}",
                "#5{WOLF @ 0}",
                "#6{MONKEY @ 0}",
                "#7{LION @ 0}"
        )
    }

    @Test
    fun updateItems_shouldChangeSomeOfItems() {
        clickMenu("Update items")
        assertList(
                "#0{TIGER @ 0}",
                "#1{WOLF @ 1}",
                "#2{MONKEY @ 0}",
                "#3{LION @ 1}"
        )
    }

    @Test
    fun moveUp_shouldPutItemFromBottomToTop() {
        clickMenu("Move up")
        assertList(
                "#3{LION @ 0}",
                "#0{TIGER @ 0}",
                "#1{WOLF @ 0}",
                "#2{MONKEY @ 0}"
        )
    }

    @Test
    fun moveDown_shouldPutItemFromTopToBottom() {
        clickMenu("Move down")
        assertList(
                "#1{WOLF @ 0}",
                "#2{MONKEY @ 0}",
                "#3{LION @ 0}",
                "#0{TIGER @ 0}"
        )
    }

    @Test
    fun resetItems_shouldSetItemsToInitialList() {
        clickMenu("Reset items")
        assertList(
                "#0{TIGER @ 0}",
                "#1{WOLF @ 0}",
                "#2{MONKEY @ 0}",
                "#3{LION @ 0}"
        )
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