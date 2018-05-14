package com.gurunars.test_utils

import android.app.Activity
import android.content.pm.ActivityInfo
import android.support.test.rule.ActivityTestRule
import android.view.View
import android.view.ViewGroup
import com.squareup.spoon.SpoonRule
import org.hamcrest.Description

import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.junit.runners.model.Statement
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

class DebugActivityRule<T: Activity>(activityClass: Class<T>) : ActivityTestRule<T>(activityClass) {
    val spoonRule = SpoonRule()

    override fun apply(base: Statement?, description: org.junit.runner.Description?): Statement {
        val spoonStmt = spoonRule.apply(base, description)
        return super.apply(object : Statement() {
            override fun evaluate() {
                try {
                    spoonStmt.evaluate()
                } catch (exe: Throwable) {
                    screenshot("screenshot-at-failure")
                    throw exe
                }
            }
        }, description)
    }

    fun screenshot(tag: String) {
        try {
            spoonRule.screenshot(activity, tag)
        } catch (exe: Exception) {}
    }

}