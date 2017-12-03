package com.gurunars.item_list.example

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.widget.TextView
import com.gurunars.android_utils.Animator
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
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
class ActivityMainRoboTest {

    private lateinit var activity: Activity

    @Before
    fun setUp() {
        Animator.enabled = false
        activity = Robolectric.setupActivity(ActivityMain::class.java)
        clickMenu(R.id.reset)
    }

    private fun clickMenu(id: Int) {
        activity.onOptionsItemSelected(RoboMenuItem(id))
    }

    @Test
    fun clickingClear_shouldShowEmptyListView() {
        clickMenu(R.id.clear)
        assertNotNull(activity.find(R.id.noItemsLabel))
    }

    private fun assertList(vararg expectedItems: String) {
        val recycler = activity.find<RecyclerView>(R.id.recyclerView)
        for (i in expectedItems.indices) {
            assertEquals(expectedItems[i], (recycler.getChildAt(i) as TextView).text.toString())
        }
    }

    @Test
    fun deletingItems_shouldLeadToPartialRemoval() {
        clickMenu(R.id.delete)
        assertList(
            "#0{TIGER @ 0}",
            "#2{MONKEY @ 0}"
        )
    }

    @Test
    fun createItems_shouldAppendItemsToTheEnd() {
        clickMenu(R.id.create)
        assertList(
            "#0{TIGER @ 0}",
            "#1{WOLF @ 0}",
            "#2{MONKEY @ 0}",
            "#3{LION @ 0}",
            "#4{TIGER @ 0}",
            "#5{WOLF @ 0}",
            "#6{MONKEY @ 0}",
            "#7{LION @ 0}"
        )
    }

    @Test
    fun updateItems_shouldChangeSomeOfItems() {
        clickMenu(R.id.update)
        assertList(
            "#0{TIGER @ 0}",
            "#1{WOLF @ 1}",
            "#2{MONKEY @ 0}",
            "#3{LION @ 1}"
        )
    }

    @Test
    fun moveUp_shouldPutItemFromBottomToTop() {
        clickMenu(R.id.moveUp)
        assertList(
            "#3{LION @ 0}",
            "#0{TIGER @ 0}",
            "#1{WOLF @ 0}",
            "#2{MONKEY @ 0}"
        )
    }

    @Test
    fun moveDown_shouldPutItemFromTopToBottom() {
        clickMenu(R.id.moveDown)
        assertList(
            "#1{WOLF @ 0}",
            "#2{MONKEY @ 0}",
            "#3{LION @ 0}",
            "#0{TIGER @ 0}"
        )
    }

    @Test
    fun resetItems_shouldSetItemsToInitialList() {
        clickMenu(R.id.reset)
        assertList(
            "#0{TIGER @ 0}",
            "#1{WOLF @ 0}",
            "#2{MONKEY @ 0}",
            "#3{LION @ 0}"
        )
    }


}