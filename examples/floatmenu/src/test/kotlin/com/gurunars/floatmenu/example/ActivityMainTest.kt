package com.gurunars.floatmenu.example

import android.app.Activity
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.view.View
import android.widget.TextView
import junit.framework.Assert.assertEquals
import org.jetbrains.anko.find
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class)
class ActivityMainRoboTest {

    private lateinit var activity: Activity

    @Before
    fun setUp() {
        activity = Robolectric.setupActivity(ActivityMain::class.java)
    }

    private fun rotate() {
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        Thread.sleep(500)
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

}