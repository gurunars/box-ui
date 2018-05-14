package com.gurunars.storybook.example

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.filters.LargeTest
import android.support.test.runner.AndroidJUnit4
import com.gurunars.storybook.ActivityStorybook
import com.gurunars.test_utils.DebugActivityRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class ActivityMainTest {

    @get:Rule
    var mActivityRule = DebugActivityRule(ActivityStorybook::class.java)

    @Test
    fun clickingButtons_shouldNotProduceExceptions() {
        onView(withId(R.id.sampleText)).check(matches(withText(R.string.title)))
    }
}