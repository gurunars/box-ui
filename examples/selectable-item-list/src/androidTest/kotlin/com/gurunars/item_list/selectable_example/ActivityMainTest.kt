package com.gurunars.item_list.selectable_example

import android.support.test.InstrumentationRegistry.getInstrumentation
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import android.support.test.espresso.ViewInteraction
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.longClick
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.gurunars.test_utils.Helpers.nthChildOf
import org.hamcrest.core.Is
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

    private fun atIndex(id: Int): ViewInteraction {
        return onView(nthChildOf(withId(R.id.recyclerView), id))
    }

    private fun validateSelection(index: Int, text: String, isSelected: Boolean) {
        atIndex(index).check(matches(withText(text)))
        atIndex(index).check(matches(ViewMatchers.withTagKey(R.id.isSelected, Is.`is`(isSelected))))
    }

    private fun assertList(vararg expectedItems: Pair<String, Boolean>) {
        for (i in expectedItems.indices) {
            validateSelection(i, expectedItems[i].first, expectedItems[i].second)
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
            Pair("#1{TIGER @ 0}", true),
            Pair("#2{WOLF @ 0}", true),
            Pair("#3{MONKEY @ 0}", false),
            Pair("#4{LION @ 0}", false)
        )
    }

    @Test
    fun deletingSelected_shouldRemoveTheItems() {
        selectTwo()
        clickMenu("Delete selected")
        Thread.sleep(700)
        assertList(
            Pair("#3{MONKEY @ 0}", false),
            Pair("#4{LION @ 0}", false)
        )
    }

    @Test
    fun updatingSelected_shouldIncrementCount() {
        selectTwo()
        clickMenu("Update selected")

        assertList(
            Pair("#1{TIGER @ 1}", true),
            Pair("#2{WOLF @ 1}", true),
            Pair("#3{MONKEY @ 0}", false),
            Pair("#4{LION @ 0}", false)
        )
    }

    @Test
    fun creatingItems_shouldNotUnselectItems() {
        selectTwo()
        clickMenu("Create items")
        Thread.sleep(700)
        assertList(
            Pair("#1{TIGER @ 0}", true),
            Pair("#2{WOLF @ 0}", true),
            Pair("#3{MONKEY @ 0}", false),
            Pair("#4{LION @ 0}", false),
            Pair("#5{TIGER @ 0}", false),
            Pair("#6{WOLF @ 0}", false),
            Pair("#7{MONKEY @ 0}", false),
            Pair("#8{LION @ 0}", false)
        )
    }

    @Test
    fun explicitSelectionMode_shouldMakeItPossibleToSelectItemstViaSingleClick() {
        clickMenu("Enable explicit selection")
        onView(nthChildOf(withId(R.id.recyclerView), 1)).perform(click())
        assertList(
            Pair("#1{TIGER @ 0}", false),
            Pair("#2{WOLF @ 0}", true),
            Pair("#3{MONKEY @ 0}", false),
            Pair("#4{LION @ 0}", false)
        )
    }

    @Test
    fun clickingClearSelection_shouldDropSelection() {
        selectTwo()
        clickMenu("Clear selection")
    }

    @After
    @Before
    fun before() {
        clickMenu("Reset items")
        Thread.sleep(700)
    }
}