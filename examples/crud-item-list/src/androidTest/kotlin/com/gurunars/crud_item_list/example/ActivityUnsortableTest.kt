package com.gurunars.crud_item_list.example

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.ViewInteraction
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.longClick
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.view.View
import com.gurunars.test_utils.Helpers.nthChildOf
import org.hamcrest.core.IsNot.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class ActivityUnsortableTest {

    private fun validateEnabled(id: Int) {
        onView(withId(id)).check(matches(isEnabled()))
    }

    private fun validateInvisible(id: Int) {
        onView(withId(id)).check(matches(not<View>(isDisplayed())))
    }

    @get:Rule
    var mActivityRule = ActivityTestRule(
        ActivityMain::class.java)

    @Before
    fun before() {
        onView(withId(R.id.reset)).perform(click())
        onView(withId(R.id.lock)).perform(click())
    }

    @After
    fun after() {
        onView(withId(R.id.reset)).perform(click())
    }

    private fun atIndex(id: Int): ViewInteraction {
        return onView(nthChildOf(withId(R.id.recyclerView), id))
    }

    @Test
    fun whenUnselectable_contextualMenuShould() {
        atIndex(3).perform(longClick())
        validateInvisible(R.id.moveUp)
        validateInvisible(R.id.moveDown)
        validateEnabled(R.id.edit)
        validateEnabled(R.id.delete)
        validateEnabled(R.id.selectAll)
    }

    @Test
    fun removingAllItems_shouldShowSpecialLayout() {
        atIndex(3).perform(longClick())
        onView(withId(R.id.selectAll)).perform(click())
        onView(withId(R.id.delete)).perform(click())
        onView(withId(R.id.noItemsLabel)).check(matches(withText("Empty")))
    }

    // TODO: add test case for item creation and opening the form again after item creation

}