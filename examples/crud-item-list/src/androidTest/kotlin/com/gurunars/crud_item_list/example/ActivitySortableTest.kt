package com.gurunars.crud_item_list.example

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.ViewInteraction
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.view.View
import com.gurunars.test_utils.Helpers.nthChildOf
import com.gurunars.test_utils.rotate
import org.hamcrest.core.Is.`is`
import org.hamcrest.core.IsNot.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class ActivitySortableTest {

    private fun rotate() {
        mActivityRule.rotate()
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
        onView(withId(id)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)))
    }

    private fun validateExists(id: Int) {
        onView(withId(id)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
    }

    private fun atIndex(id: Int): ViewInteraction {
        return onView(nthChildOf(withId(R.id.recyclerView), id))
    }

    private fun validateSelection(index: Int, text: String, isSelected: Boolean) {
        onView(nthChildOf(nthChildOf(withId(R.id.recyclerView), index), 1)).check(matches(withText(text)))
        atIndex(index).check(matches(withTagKey(R.id.isSelected, `is`(isSelected))))
    }

    private fun sleep() {
        Thread.sleep(2100)
    }

    @Test
    fun selectingOne_shouldOpenContextualMenu() {
        validateDoesNotExist(R.id.menuPane)
        atIndex(1).perform(longClick())
        rotate()
        validateExists(R.id.menuPane)
        validateEnabled(R.id.edit)
        validateEnabled(R.id.moveUp)
        validateEnabled(R.id.moveDown)
        validateEnabled(R.id.delete)
        validateEnabled(R.id.selectAll)
    }

    @Test
    fun deselectingLast_shouldCloseContextualMenu() {
        validateDoesNotExist(R.id.menuPane)
        atIndex(0).perform(longClick())
        validateExists(R.id.menuPane)
        rotate()
        validateExists(R.id.menuPane)
        atIndex(0).perform(click())
        rotate()
        validateDoesNotExist(R.id.menuPane)
    }

    @Test
    fun clickingTick_shouldCloseContextualMenu() {
        validateDoesNotExist(R.id.menuPane)
        atIndex(0).perform(longClick())
        validateExists(R.id.menuPane)
        rotate()
        validateExists(R.id.menuPane)
        onView(withId(R.id.openFab)).perform(click())
        rotate()
        validateDoesNotExist(R.id.menuPane)
    }

    @Test
    fun selectingTopItem_shouldDisableMoveUp() {
        atIndex(0).perform(longClick())
        rotate()
        validateDisabled(R.id.moveUp)
        validateEnabled(R.id.edit)
        validateEnabled(R.id.moveDown)
        validateEnabled(R.id.delete)
        validateEnabled(R.id.selectAll)
    }

    @Test
    fun selectingBottomItem_shouldDisableMoveDown() {
        atIndex(3).perform(longClick())
        rotate()
        validateEnabled(R.id.moveUp)
        validateEnabled(R.id.edit)
        validateDisabled(R.id.moveDown)
        validateEnabled(R.id.delete)
        validateEnabled(R.id.selectAll)
    }

    @Test
    fun selectingUnsolidChunk_shouldDisableMove() {
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
    fun selectingSolidChunk_shouldEnableMove() {
        atIndex(1).perform(longClick())
        rotate()
        atIndex(2).perform(click())
        rotate()
        validateDisabled(R.id.edit)
        validateEnabled(R.id.moveUp)
        validateEnabled(R.id.moveDown)
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
        validateEnabled(R.id.edit)
        validateEnabled(R.id.moveDown)
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
    fun removingAllItems_shouldShowSpecialDefaultLayout() {
        atIndex(1).perform(longClick())
        rotate()
        onView(withId(R.id.selectAll)).perform(click())
        onView(withId(R.id.delete)).perform(click())
        onView(withId(R.id.noItemsLabel)).check(matches(withText("Empty")))
    }

    @Test
    fun testSelectMoveUpAndReset() {
        atIndex(1).perform(longClick())
        onView(withId(R.id.moveUp)).perform(click())
        onView(withId(R.id.reset)).perform(click())
        sleep()
        validateSelection(0, "#1{LION @ 0}", false)
        validateSelection(1, "#2{TIGER @ 0}", false)
    }

    // TODO: flaky
    @Test
    fun testCopyAndPaste() {
        atIndex(1).perform(longClick())
        atIndex(2).perform(click())
        rotate()
        onView(withId(R.id.copy)).perform(click())
        onView(withId(R.id.paste)).perform(click())
        sleep()
        rotate()
        atIndex(4).perform(click())
        atIndex(5).perform(click())

        validateSelection(1, "#2{TIGER @ 0}", true)
        validateSelection(2, "#3{MONKEY @ 0}", true)
        validateSelection(3, "#5{TIGER @ 0}", false)
        validateSelection(4, "#6{MONKEY @ 0}", true)
        validateSelection(5, "#4{WOLF @ 0}", true)
    }

    @Test
    fun testSelectMoveDownAndReset() {
        atIndex(2).perform(longClick())
        onView(withId(R.id.moveDown)).perform(click())
        onView(withId(R.id.reset)).perform(click())
        sleep()
        validateSelection(2, "#3{MONKEY @ 0}", false)
        validateSelection(3, "#4{WOLF @ 0}", false)
    }

    // TODO: flaky
    @Test
    fun editingItem_shouldIncrementVersion() {
        atIndex(3).perform(longClick())
        onView(withId(R.id.edit)).perform(click())
        onView(withId(R.id.increment)).perform(click())
        rotate()
        onView(withId(R.id.save)).perform(click())
        // Save operation updates the value asynchronously - we need to wait
        sleep()
        rotate()
        validateSelection(3, "#4{WOLF @ 1}", false)
    }

    @Test
    fun creatingItem_shouldIncrementItemListSize() {
        onView(withId(R.id.openFab)).perform(click())
        rotate()
        onView(withTagValue(`is`("LION"))).perform(click())
        onView(withId(R.id.versionValue)).perform(typeText("4"))
        onView(withId(R.id.increment)).perform(click())
        rotate()
        onView(withId(R.id.save)).perform(click())
        sleep()
        validateSelection(4, "#5{LION @ 5}", false)
    }

    @Test
    fun longPressingPlus_shouldOpenContextualMenu() {
        onView(withId(R.id.openFab)).perform(longClick())
        rotate()
        validateExists(R.id.menuPane)
        validateDisabled(R.id.paste)
        validateDisabled(R.id.copy)
        validateDisabled(R.id.edit)
        validateDisabled(R.id.moveUp)
        validateDisabled(R.id.moveDown)
        validateDisabled(R.id.delete)
        validateEnabled(R.id.selectAll)
    }

    @Test
    fun explicitContextualMenu_shouldEnableSelectionViaOneClick() {
        onView(withId(R.id.openFab)).perform(longClick())
        rotate()
        atIndex(2).perform(click())
        validateSelection(1, "#2{TIGER @ 0}", false)
        validateSelection(2, "#3{MONKEY @ 0}", true)
        validateSelection(3, "#4{WOLF @ 0}", false)
    }

    @Before
    fun before() {
        onView(withId(R.id.reset)).perform(click())
        onView(withId(R.id.unlock)).perform(click())
        sleep()
    }

    @After
    fun waitd() {
        //Thread.sleep(100000)
    }

}