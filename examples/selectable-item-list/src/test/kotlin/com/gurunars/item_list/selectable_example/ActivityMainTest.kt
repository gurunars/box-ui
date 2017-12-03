package com.gurunars.item_list.selectable_example

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.gurunars.android_utils.Animator
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
class ActivityMainTest {

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

    private fun childAt(pos: Int): View {
        val recycler = activity.find<RecyclerView>(R.id.recyclerView)
        return recycler.getChildAt(pos)
    }

    private fun validateSelection(index: Int, text: String, isSelected: Boolean) {
        val view = (childAt(index) as TextView)
        assertEquals(text, view.text.toString())
        assertEquals(isSelected, view.getTag(R.id.isSelected))
    }

    private fun assertList(vararg expectedItems: Pair<String, Boolean>) {
        for (i in expectedItems.indices) {
            validateSelection(i, expectedItems[i].first, expectedItems[i].second)
        }
    }

    private fun selectTwo() {
        childAt(0).performLongClick()
        childAt(1).performClick()
    }

    @Test
    fun longClickingOneAndClickingAnother_shouldSelectTwoItems() {
        selectTwo()
        assertList(
            Pair("#0{TIGER @ 0}", true),
            Pair("#1{WOLF @ 0}", true),
            Pair("#2{MONKEY @ 0}", false),
            Pair("#3{LION @ 0}", false)
        )
    }

    @Test
    fun deletingSelected_shouldRemoveTheItems() {
        selectTwo()
        clickMenu(R.id.delete_selected)
        assertList(
            Pair("#2{MONKEY @ 0}", false),
            Pair("#3{LION @ 0}", false)
        )
    }

    @Test
    fun updatingSelected_shouldIncrementCount() {
        selectTwo()
        clickMenu(R.id.update_selected)
        assertList(
            Pair("#0{TIGER @ 1}", true),
            Pair("#1{WOLF @ 1}", true),
            Pair("#2{MONKEY @ 0}", false),
            Pair("#3{LION @ 0}", false)
        )
    }

    @Test
    fun creatingItems_shouldNotUnselectItems() {
        selectTwo()
        clickMenu(R.id.create)
        assertList(
            Pair("#0{TIGER @ 0}", true),
            Pair("#1{WOLF @ 0}", true),
            Pair("#2{MONKEY @ 0}", false),
            Pair("#3{LION @ 0}", false),
            Pair("#4{TIGER @ 0}", false),
            Pair("#5{WOLF @ 0}", false),
            Pair("#6{MONKEY @ 0}", false),
            Pair("#7{LION @ 0}", false)
        )
    }

    @Test
    fun clickingClearSelection_shouldDropSelection() {
        selectTwo()
        clickMenu(R.id.clear_selection)
        assertList(
            Pair("#0{TIGER @ 0}", false),
            Pair("#1{WOLF @ 0}", false),
            Pair("#2{MONKEY @ 0}", false),
            Pair("#3{LION @ 0}", false)
        )
    }

}