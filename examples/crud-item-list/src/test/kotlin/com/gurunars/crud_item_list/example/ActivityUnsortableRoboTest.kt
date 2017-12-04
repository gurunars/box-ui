package com.gurunars.crud_item_list.example

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.gurunars.android_utils.Animator
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
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
class ActivityUnsortableRoboTest {

    private lateinit var activity: Activity

    @Before
    fun setUp() {
        Animator.enabled = false
        activity = Robolectric.setupActivity(ActivityMain::class.java)
        clickMenu(R.id.reset)
        clickMenu(R.id.lock)
    }

    private fun clickMenu(id: Int) {
        activity.onOptionsItemSelected(RoboMenuItem(id))
    }

    private fun validateInvisible(id: Int) {
        assertEquals(View.GONE, activity.find<View>(id).visibility)
    }

    private fun validateEnabled(id: Int) {
        assertTrue(activity.find<View>(id).isEnabled)
    }

    private fun click(id: Int) {
        activity.find<View>(id).performClick()
        Robolectric.flushBackgroundThreadScheduler()
        Robolectric.flushForegroundThreadScheduler()
    }

    private fun atIndex(index: Int) =
        activity.find<RecyclerView>(R.id.recyclerView).getChildAt(index)

    @Test
    fun whenUnselectable_contextualMenuShouldNotHaveUpAndDownButtons() {
        atIndex(3).performLongClick()
        validateInvisible(R.id.moveUp)
        validateInvisible(R.id.moveDown)
        validateEnabled(R.id.delete)
        validateEnabled(R.id.selectAll)
    }

    @Test
    fun removingAllItems_shouldShowSpecialLayout() {
        atIndex(3).performLongClick()
        click(R.id.selectAll)
        click(R.id.delete)
        assertEquals(
            "Empty",
            activity.find<TextView>(R.id.noItemsLabel).text.toString()
        )
    }

    // TODO: add test case for item creation and opening the form again after item creation

}