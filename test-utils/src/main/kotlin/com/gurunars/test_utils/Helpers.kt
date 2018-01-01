package com.gurunars.test_utils

import android.content.pm.ActivityInfo
import android.support.test.rule.ActivityTestRule
import android.view.View
import android.view.ViewGroup

import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import java.lang.Thread.sleep

fun ActivityTestRule<*>.rotate() {
    activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    sleep(500)
}

object Helpers {

    fun nthChildOf(parentMatcher: Matcher<View>, childPosition: Int): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                parentMatcher.describeTo(description.apply {
                    appendText("position $childPosition of parent ")
                })
            }

            public override fun matchesSafely(view: View): Boolean {
                if (view.parent !is ViewGroup) return false
                val parent = view.parent as ViewGroup

                return parentMatcher.matches(parent)
                    && parent.childCount > childPosition
                    && parent.getChildAt(childPosition) == view
            }
        }
    }
}
