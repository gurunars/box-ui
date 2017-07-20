package com.gurunars.android_utils.example

import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4

import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText


@RunWith(AndroidJUnit4::class)
@LargeTest
class ActivityMainTest {

    @get:Rule
    var mActivityRule = ActivityTestRule(
            ActivityMain::class.java)

    private fun validateText(text: String) {
        mActivityRule.activity.finish()
        mActivityRule.launchActivity(null)
        onView(withId(R.id.payloadView)).check(matches(withText(text)))
    }

    @Test
    fun clickingButtons_shouldNotProduceExceptions() {
        onView(withId(R.id.clear)).perform(click())
        onView(withId(R.id.disabled)).perform(click())
        validateText("Empty")
        onView(withId(R.id.set)).perform(click())
        validateText("Configured")
        onView(withId(R.id.clear)).perform(click())
        validateText("Empty")
    }

}