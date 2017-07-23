package com.gurunars.floatmenu.example

import android.content.pm.ActivityInfo
import android.support.test.InstrumentationRegistry.getInstrumentation
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import android.support.test.espresso.ViewInteraction
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.longClick
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class ActivityMainTest {

    @get:Rule
    var mActivityRule = ActivityTestRule(ActivityMain::class.java)

    private fun rotate() {
        mActivityRule.activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        mActivityRule.activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        Thread.sleep(500)
    }

    private fun fab(): ViewInteraction {
        return onView(withId(R.id.openFab))
    }

    private fun checkNotification(title: String) {
        onView(withId(R.id.notificationView)).check(matches(withText(title)))
        onView(withId(R.id.notificationView)).perform(longClick())
    }

    private fun checkFab(iconDescription: String, menuContentDescription: String) {
        fab().check(matches(withContentDescription(menuContentDescription)))
        onView(withId(R.id.iconView)).check(matches(
                withContentDescription(iconDescription)))
    }

    @Before
    fun before() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().targetContext)
        onView(withText("Reset")).perform(click())
    }

    @Test
    fun clickingFab_shouldOpenAndCloseMenu() {
        rotate()
        checkFab("|BG:-4419697|IC:-16777216", "LH:false")
        fab().perform(click())
        rotate()
        checkFab("|BG:-1|IC:-16777216", "LH:false")
        fab().perform(click())
        rotate()
        checkFab("|BG:-4419697|IC:-16777216", "LH:false")
    }

    @Test
    fun clickingText() {
        onView(withId(R.id.textView)).perform(click())
        checkNotification("Content Text Clicked")
    }

    @Test
    fun clickingButtonInMenu() {
        rotate()
        fab().perform(click())
        rotate()
        onView(withId(R.id.button)).perform(click())
        checkNotification("Menu Button Clicked")
    }

    @Test
    fun togglingMenuDecoration_shouldChangeBackgroundAndForeground() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().targetContext)
        onView(withText("Toggle button color")).perform(click())
        rotate()
        checkFab("|BG:-7667712|IC:-1", "LH:false")
    }

    private fun toggleBg() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().targetContext)
        onView(withText("Toggle background")).perform(click())
        fab().perform(click())
    }

    @Test
    fun togglingBackground_shouldMakeBackgroundTranslucent() {
        toggleBg()
        rotate()
        onView(withId(R.id.textView)).perform(click())
        checkNotification("Content Text Clicked")
    }

    @Test
    fun togglingBackground_shouldLeaveButtonClickable() {
        toggleBg()
        rotate()
        onView(withId(R.id.button)).perform(click())
        checkNotification("Menu Button Clicked")
    }

    @Test
    fun togglingBackground_shouldLeaveFrameClickable() {
        toggleBg()
        rotate()
        onView(withId(R.id.buttonFrame)).perform(click())
        checkNotification("Menu Button Frame Clicked")
    }

    @Test
    fun togglingLeftHand_shouldChangeTheProperties() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().targetContext)
        onView(withText("Toggle left/right hand")).perform(click())
        rotate()
        checkFab("|BG:-4419697|IC:-16777216", "LH:true")
    }

}