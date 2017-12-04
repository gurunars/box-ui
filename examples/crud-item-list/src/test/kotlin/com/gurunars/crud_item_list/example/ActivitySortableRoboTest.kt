package com.gurunars.crud_item_list.example

import android.app.Activity
import android.content.pm.ActivityInfo
import android.support.v7.widget.RecyclerView
import android.text.SpannableStringBuilder
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.gurunars.android_utils.Animator
import junit.framework.Assert
import junit.framework.Assert.assertEquals
import org.jetbrains.anko.find
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.fakes.RoboMenuItem

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class ActivitySortableRoboTest {

    private lateinit var activity: Activity

    @Before
    fun setUp() {
        Animator.enabled = false
        activity = Robolectric.setupActivity(ActivityMain::class.java)
        clickMenu(R.id.reset)
        clickMenu(R.id.lock)
    }

    private fun clickMenu(id: Int) =
        activity.onOptionsItemSelected(RoboMenuItem(id))

    private fun validateExists(id: Int) =
        Assert.assertEquals(View.VISIBLE, activity.find<View>(id).visibility)

    private fun validateDoesNotExist(id: Int) =
        Assert.assertEquals(View.GONE, activity.find<View>(id).visibility)

    private fun validateEnabled(id: Int) =
        Assert.assertTrue(activity.find<View>(id).isEnabled)

    private fun validateDisabled(id: Int) =
        Assert.assertFalse(activity.find<View>(id).isEnabled)

    private fun atIndex(index: Int) =
        activity.find<RecyclerView>(R.id.recyclerView).getChildAt(index)

    private fun validateSelection(index: Int, text: String, isSelected: Boolean) {
        val view = (atIndex(index) as TextView)
        Assert.assertEquals(text, view.text.toString())
        Assert.assertEquals(isSelected, view.getTag(R.id.isSelected))
    }

    private fun rotate() {
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    private fun click(id: Int) {
        activity.find<View>(id).performClick()
        Robolectric.flushBackgroundThreadScheduler()
        Robolectric.flushForegroundThreadScheduler()
    }

    @Test
    fun selectingOne_shouldOpenContextualMenu() {
        validateDoesNotExist(R.id.menuPane)
        atIndex(1).performLongClick()
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
        atIndex(0).performLongClick()
        validateExists(R.id.menuPane)
        rotate()
        validateExists(R.id.menuPane)
        atIndex(0).performClick()
        rotate()
        validateDoesNotExist(R.id.menuPane)
    }

    @Test
    fun clickingTick_shouldCloseContextualMenu() {
        validateDoesNotExist(R.id.menuPane)
        atIndex(0).performLongClick()
        validateExists(R.id.menuPane)
        rotate()
        validateExists(R.id.menuPane)
        click(R.id.openFab)
        rotate()
        validateDoesNotExist(R.id.menuPane)
    }

    @Test
    fun selectingTopItem_shouldDisableMoveUp() {
        atIndex(0).performLongClick()
        rotate()
        validateDisabled(R.id.moveUp)
        validateEnabled(R.id.edit)
        validateEnabled(R.id.moveDown)
        validateEnabled(R.id.delete)
        validateEnabled(R.id.selectAll)
    }

    @Test
    fun selectingBottomItem_shouldDisableMoveDown() {
        atIndex(3).performLongClick()
        rotate()
        validateEnabled(R.id.moveUp)
        validateEnabled(R.id.edit)
        validateDisabled(R.id.moveDown)
        validateEnabled(R.id.delete)
        validateEnabled(R.id.selectAll)
    }

    @Test
    fun selectingUnsolidChunk_shouldDisableMove() {
        atIndex(0).performLongClick()
        rotate()
        atIndex(3).performClick()
        rotate()
        validateDisabled(R.id.moveUp)
        validateDisabled(R.id.moveDown)
        validateDisabled(R.id.edit)
        validateEnabled(R.id.delete)
        validateEnabled(R.id.selectAll)
    }

    @Test
    fun selectingSolidChunk_shouldEnableMove() {
        atIndex(1).performLongClick()
        rotate()
        atIndex(2).performClick()
        rotate()
        validateDisabled(R.id.edit)
        validateEnabled(R.id.moveUp)
        validateEnabled(R.id.moveDown)
        validateEnabled(R.id.delete)
        validateEnabled(R.id.selectAll)
    }

    @Test
    fun movingSelectionToTop_shouldDisableMoveUp() {
        atIndex(1).performLongClick()
        rotate()
        click(R.id.moveUp)
        rotate()
        validateDisabled(R.id.moveUp)
        validateEnabled(R.id.edit)
        validateEnabled(R.id.moveDown)
        validateEnabled(R.id.delete)
        validateEnabled(R.id.selectAll)
    }

    @Test
    fun movingSelectionToBottom_shouldDisableMoveDown() {
        atIndex(2).performLongClick()
        rotate()
        click(R.id.moveDown)
        rotate()
        validateEnabled(R.id.moveUp)
        validateDisabled(R.id.moveDown)
        validateEnabled(R.id.edit)
        validateEnabled(R.id.delete)
        validateEnabled(R.id.selectAll)
    }

    @Test
    fun removingAllItems_shouldShowSpecialDefaultLayout() {
        atIndex(1).performLongClick()
        rotate()
        click(R.id.selectAll)
        click(R.id.delete)
        assertEquals("Empty", activity.find<TextView>(R.id.noItemsLabel).text.toString())
    }

    @Test
    fun testSelectMoveUpAndReset() {
        atIndex(1).performLongClick()
        click(R.id.moveUp)
        click(R.id.reset)
        validateSelection(0, "#1{LION @ 0}", false)
        validateSelection(1, "#2{TIGER @ 0}", true)
    }

    @Test
    fun testCopyAndPaste() {
        atIndex(1).performLongClick()
        atIndex(2).performClick()
        rotate()
        click(R.id.copy)
        click(R.id.paste)
        rotate()
        atIndex(4).performClick()
        atIndex(5).performClick()

        validateSelection(1, "#2{TIGER @ 0}", true)
        validateSelection(2, "#3{MONKEY @ 0}", true)
        validateSelection(3, "#4{WOLF @ 0}", false)
        validateSelection(4, "#5{TIGER @ 0}", true)
        validateSelection(5, "#6{MONKEY @ 0}", true)
    }

    @Test
    fun testSelectMoveDownAndReset() {
        atIndex(2).performLongClick()
        click(R.id.moveDown)
        click(R.id.reset)
        validateSelection(2, "#3{MONKEY @ 0}", true)
        validateSelection(3, "#4{WOLF @ 0}", false)
    }

    @Test
    fun editingItem_shouldIncrementVersion() {
        atIndex(3).performLongClick()
        click(R.id.edit)
        click(R.id.increment)
        rotate()
        click(R.id.save)
        // Save operation updates the value asynchronously - we need to wait
        rotate()
        validateSelection(3, "#4{WOLF @ 1}", false)
    }

    @Test
    fun creatingItem_shouldIncrementItemListSize() {
        click(R.id.openFab)
        rotate()
        activity.findByTag("LION").first().performClick()
        activity.find<EditText>(R.id.versionValue).text = SpannableStringBuilder("4")
        click(R.id.increment)
        rotate()
        click(R.id.save)
        validateSelection(4, "#5{LION @ 5}", false)
    }

}