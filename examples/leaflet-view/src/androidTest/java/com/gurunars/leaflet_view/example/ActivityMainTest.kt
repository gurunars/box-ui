package com.gurunars.leaflet_view.example

import android.content.pm.ActivityInfo
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.view.View
import android.view.ViewGroup

import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import android.support.test.InstrumentationRegistry.getInstrumentation
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.replaceText
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.hasSibling
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import com.gurunars.test_utils.Helpers.nthChildOf
import org.hamcrest.core.AllOf.allOf

@RunWith(AndroidJUnit4::class)
@LargeTest
class ActivityMainTest {

    @Rule
    var mActivityRule = ActivityTestRule(
            ActivityMain::class.java)

    private fun createPage(title: String) {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().targetContext)
        onView(withText("Create page")).perform(click())
        onView(allOf(isDisplayed(), withId(R.id.pageTitle))).perform(replaceText(title))
        onView(withId(android.R.id.button1)).perform(click())
    }

    private fun checkTitle(text: String) {
        onView(allOf(isDisplayed(), withId(R.id.pageTitle))).check(matches(withText(text)))
        onView(nthChildOf(withId(R.id.action_bar), 0)).check(matches(withText(text)))
    }

    private fun ensureEmpty() {
        onView(withId(R.id.noPageText)).check(matches(withText("Empty")))
        onView(nthChildOf(withId(R.id.action_bar), 0)).check(matches(withText("Empty")))
    }

    @Test
    fun initialOpen_shouldShowEmptyView() {
        ensureEmpty()
    }

    @Test
    fun creatingBlankNewPage_shouldNavigateToIt() {
        createPage("Page title")
        checkTitle("Page title")
    }

    @Test
    fun creatingNewPage_shouldNotNavigateAnywhereToIt() {
        createPage("Page title")
        createPage("aaa")
        createPage("zzz")
        checkTitle("Page title")
    }

    @Test
    fun deletingTheOnlyPage_shouldShowEmptyView() {
        createPage("Page title")
        openActionBarOverflowOrOptionsMenu(getInstrumentation().targetContext)
        onView(withText("Delete page")).perform(click())
        ensureEmpty()
    }

    @Test
    fun editingThePage_shouldChangeIt() {
        createPage("Page title")
        openActionBarOverflowOrOptionsMenu(getInstrumentation().targetContext)
        onView(withText("Edit page")).perform(click())
        onView(withId(R.id.pageTitle)).perform(replaceText("Edited title"))
        onView(withId(android.R.id.button1)).perform(click())
        onView(withId(R.id.pageTitle)).check(matches(withText("Edited title")))
        checkTitle("Edited title")
    }

    @Test
    fun goingToPage_shouldNavigateToIt() {
        createPage("Page title 1")
        createPage("Page title 2")
        createPage("Page title 3")
        openActionBarOverflowOrOptionsMenu(getInstrumentation().targetContext)
        onView(withText("Go to page")).perform(click())
        onView(withText("Page title 1")).perform(click())
        onView(nthChildOf(withId(R.id.action_bar), 0)).check(matches(withText("Page title 1")))
    }

    private fun goToPage(title: String) {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().targetContext)
        onView(withText("Go to page")).perform(click())
        onView(withText(title)).perform(click())
    }

    private fun restart() {
        mActivityRule.activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        mActivityRule.activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    @Test
    fun enteredEditTextInPages_shouldNotBeRetainedWhenNavigatingAway() {
        createPage("Page title 1")
        createPage("Page title 2")
        createPage("Page title 3")
        createPage("Page title 4")
        createPage("Page title 5")
        createPage("Page title 6")
        onView(allOf(hasSibling(withText("Page title 1")), withId(R.id.textEdit))).perform(replaceText("Text to retain"))
        goToPage("Page title 6")
        // Should be empty on page 6
        onView(allOf(hasSibling(withText("Page title 6")), withId(R.id.textEdit))).check(matches(withText("")))
        goToPage("Page title 1")
        // Should have retained entered title
        onView(allOf(hasSibling(withText("Page title 1")), withId(R.id.textEdit))).check(matches(withText("")))
    }

    @Test
    fun enteredEditTextInPages_shouldBeRetainedAfterOrientationChange() {
        createPage("Page title 1")
        onView(allOf(hasSibling(withText("Page title 1")), withId(R.id.textEdit))).perform(replaceText("Text to retain"))
        restart()
        onView(allOf(hasSibling(withText("Page title 1")), withId(R.id.textEdit))).check(matches(withText("Text to retain")))
    }

    @Before
    fun clear() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().targetContext)
        onView(withText("Clear")).perform(click())
    }

}