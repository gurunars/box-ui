package com.gurunars.floatmenu.example

import android.app.Activity
import android.content.pm.ActivityInfo
import android.graphics.Color
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
class ActivityMainRoboTest {

    private lateinit var activity: Activity

    @Before
    fun setUp() {
        Animator.enabled = false
        activity = Robolectric.setupActivity(ActivityMain::class.java)
    }

    private fun rotate() {
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    private fun click(id: Int) {
        activity.find<View>(id).performClick()
    }

    private fun checkFab(iconDescription: String) {
        assertEquals(iconDescription, activity.find<View>(R.id.iconView).contentDescription.toString())
    }

    @Test
    fun clickingFab_shouldOpenAndCloseMenu() {
        rotate()
        checkFab("|BG:${Color.YELLOW}|IC:${Color.BLACK}")
        click(R.id.openFab)
        rotate()
        checkFab("|BG:${Color.WHITE}|IC:${Color.BLACK}")
        click(R.id.openFab)
        rotate()
        checkFab("|BG:${Color.YELLOW}|IC:${Color.BLACK}")
    }

    private fun checkNotification(title: String) {
        val view = activity.find<TextView>(R.id.notificationView)
        assertEquals(title, view.text.toString())
        view.performLongClick()
    }

    @Test
    fun clickingText() {
        click(R.id.textView)
        checkNotification("Content Text Clicked")
    }

    @Test
    fun clickingButtonInMenu() {
        rotate()
        click(R.id.openFab)
        rotate()
        click(R.id.button)
        checkNotification("Menu Button Clicked")
    }

    private fun toggleBg() {
        activity.onOptionsItemSelected(RoboMenuItem(R.id.toggleBackground))
        click(R.id.openFab)
    }

    @Test
    fun togglingBackground_shouldMakeBackgroundTranslucent() {
        toggleBg()
        rotate()
        click(R.id.textView)
        checkNotification("Content Text Clicked")
    }

    @Test
    fun togglingBackground_shouldLeaveButtonClickable() {
        toggleBg()
        rotate()
        click(R.id.button)
        checkNotification("Menu Button Clicked")
    }

    @Test
    fun togglingBackground_shouldLeaveFrameClickable() {
        toggleBg()
        rotate()
        click(R.id.buttonFrame)
        checkNotification("Menu Button Frame Clicked")
    }

}