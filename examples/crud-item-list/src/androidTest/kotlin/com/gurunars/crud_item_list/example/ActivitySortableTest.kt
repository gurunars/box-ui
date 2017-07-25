package com.gurunars.crud_item_list.example

import android.content.pm.ActivityInfo
import android.support.test.espresso.ViewInteraction
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
import android.support.test.espresso.action.ViewActions.longClick
import android.support.test.espresso.assertion.ViewAssertions.doesNotExist
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isEnabled
import android.support.test.espresso.matcher.ViewMatchers.withContentDescription
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.view.View
import com.gurunars.test_utils.Helpers.nthChildOf
import org.hamcrest.core.IsNot.not

@RunWith(AndroidJUnit4::class)
@LargeTest
class ActivitySortableTest {

    private fun rotate() {
        mActivityRule.activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        mActivityRule.activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        Thread.sleep(500)
    }

    @get:Rule
    var mActivityRule = ActivityTestRule(
            ActivityMain::class.java)

    private fun validateEnabled(id: Int) {
        onView(withId(id)).check(matches(isEnabled()))
    }

    private fun validateDisabled(id: Int) {
        onView(withId(id)).check(matches(not<View>(isEnabled())))
    }

    private fun validateDoesNotExist(id: Int) {
        onView(withId(id)).check(doesNotExist())
    }

    private fun atIndex(id: Int): ViewInteraction {
        return onView(nthChildOf(withId(R.id.recyclerView), id))
    }

    @Test
    fun selectingOne_shouldOpenContextualMenu() {
        validateDoesNotExist(R.id.moveUp)
        validateDoesNotExist(R.id.moveDown)
        validateDoesNotExist(R.id.edit)
        validateDoesNotExist(R.id.delete)
        validateDoesNotExist(R.id.selectAll)
        atIndex(1).perform(longClick())
        rotate()
        validateEnabled(R.id.moveUp)
        validateEnabled(R.id.moveDown)
        validateEnabled(R.id.edit)
        validateEnabled(R.id.delete)
        validateEnabled(R.id.selectAll)
    }

    @Test
    fun deselectingLast_shouldCloseContextualMenu() {
        atIndex(0).perform(longClick())
        rotate()
        atIndex(0).perform(click())
        rotate()
        validateDoesNotExist(R.id.moveUp)
        validateDoesNotExist(R.id.moveDown)
        validateDoesNotExist(R.id.edit)
        validateDoesNotExist(R.id.delete)
        validateDoesNotExist(R.id.selectAll)
    }

    @Test
    fun clickingCross_shouldCloseContextualMenu() {
        atIndex(0).perform(longClick())
        rotate()
        onView(withId(R.id.openFab)).perform(click())
        rotate()
        validateDoesNotExist(R.id.moveUp)
        validateDoesNotExist(R.id.moveDown)
        validateDoesNotExist(R.id.edit)
        validateDoesNotExist(R.id.delete)
        validateDoesNotExist(R.id.selectAll)
    }

    @Test
    fun selectingTopItem_shouldDisableMoveUp() {
        atIndex(0).perform(longClick())
        rotate()
        validateDisabled(R.id.moveUp)
        validateEnabled(R.id.moveDown)
        validateEnabled(R.id.edit)
        validateEnabled(R.id.delete)
        validateEnabled(R.id.selectAll)
    }

    @Test
    fun selectingBottomItem_shouldDisableMoveDown() {
        atIndex(3).perform(longClick())
        rotate()
        validateEnabled(R.id.moveUp)
        validateDisabled(R.id.moveDown)
        validateEnabled(R.id.edit)
        validateEnabled(R.id.delete)
        validateEnabled(R.id.selectAll)
    }

    @Test
    fun selectingUnsolidChunk_shouldDisableMoveAndEdit() {
        atIndex(0).perform(longClick())
        rotate()
        atIndex(3).perform(click())
        rotate()
        validateDisabled(R.id.moveUp)
        validateDisabled(R.id.moveDown)
        validateDisabled(R.id.edit)
        validateEnabled(R.id.delete)
        validateEnabled(R.id.selectAll)
    }

    @Test
    fun selectingSolidChunk_shouldEnableMoveButDisableEdit() {
        atIndex(1).perform(longClick())
        rotate()
        atIndex(2).perform(click())
        rotate()
        validateEnabled(R.id.moveUp)
        validateEnabled(R.id.moveDown)
        validateDisabled(R.id.edit)
        validateEnabled(R.id.delete)
        validateEnabled(R.id.selectAll)
    }

    @Test
    fun movingSelectionToTop_shouldDisableMoveUp() {
        atIndex(1).perform(longClick())
        rotate()
        onView(withId(R.id.moveUp)).perform(click())
        rotate()
        validateDisabled(R.id.moveUp)
        validateEnabled(R.id.moveDown)
        validateEnabled(R.id.edit)
        validateEnabled(R.id.delete)
        validateEnabled(R.id.selectAll)
    }

    @Test
    fun movingSelectionToBottom_shouldDisableMoveDown() {
        atIndex(2).perform(longClick())
        rotate()
        onView(withId(R.id.moveDown)).perform(click())
        rotate()
        validateEnabled(R.id.moveUp)
        validateDisabled(R.id.moveDown)
        validateEnabled(R.id.edit)
        validateEnabled(R.id.delete)
        validateEnabled(R.id.selectAll)
    }

    @Test
    fun editingItem_shouldIncrementVersion() {
        atIndex(3).perform(longClick())
        rotate()
        onView(withId(R.id.edit)).perform(click())
        rotate()
        atIndex(3).check(matches(withText("#3{WOLF @ 1}|true")))
    }

    @Test
    fun creatingItem_shouldIncrementItemListSize() {
        onView(withId(R.id.openFab)).perform(click())
        rotate()
        onView(withId(R.id.lion)).perform(click())
        rotate()
        atIndex(4).check(matches(withText("#4{LION @ 0}|false")))
    }

    @Test
    fun removingAllItems_shouldShowSpecialDefaultLayout() {
        atIndex(1).perform(longClick())
        rotate()
        onView(withId(R.id.selectAll)).perform(click())
        onView(withId(R.id.delete)).perform(click())
        onView(withId(R.id.noItemsLabel)).check(matches(withText("No items at all")))
    }

    @Test
    fun leftAndRightHandSwitch() {
        atIndex(3).perform(longClick())
        rotate()
        onView(withId(R.id.contextualMenu)).check(matches(withContentDescription("RIGHT HANDED")))
        openActionBarOverflowOrOptionsMenu(getInstrumentation().targetContext)
        onView(withText("Left handed")).perform(click())
        rotate()
        onView(withId(R.id.contextualMenu)).check(matches(withContentDescription("LEFT HANDED")))
        openActionBarOverflowOrOptionsMenu(getInstrumentation().targetContext)
        onView(withText("Right handed")).perform(click())
        rotate()
        onView(withId(R.id.contextualMenu)).check(matches(withContentDescription("RIGHT HANDED")))
    }

    @Test
    fun testSelectMoveUpAndReset() {
        atIndex(1).perform(longClick())
        onView(withId(R.id.moveUp)).perform(click())
        onView(withId(R.id.reset)).perform(click())
        atIndex(0).check(matches(withText("#0{LION @ 0}|false")))
        atIndex(1).check(matches(withText("#1{TIGER @ 0}|true")))
    }

    @Test
    fun testSelectMoveDownAndReset() {
        atIndex(2).perform(longClick())
        onView(withId(R.id.moveDown)).perform(click())
        onView(withId(R.id.reset)).perform(click())
        atIndex(2).check(matches(withText("#2{MONKEY @ 0}|true")))
        atIndex(3).check(matches(withText("#3{WOLF @ 0}|false")))
    }

    @Before
    fun before() {
        onView(withId(R.id.reset)).perform(click())
        onView(withId(R.id.unlock)).perform(click())
        openActionBarOverflowOrOptionsMenu(getInstrumentation().targetContext)
        onView(withText("Right handed")).perform(click())
    }

    @After
    fun after() {
        onView(withId(R.id.reset)).perform(click())
    }

}