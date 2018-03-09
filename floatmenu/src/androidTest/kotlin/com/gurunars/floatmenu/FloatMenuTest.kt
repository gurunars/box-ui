package com.gurunars.floatmenu

import android.content.Context
import android.os.SystemClock
import android.support.test.InstrumentationRegistry
import android.support.test.filters.LargeTest
import android.support.test.runner.AndroidJUnit4
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import com.gurunars.android_utils.Icon
import com.gurunars.box.IBox
import com.gurunars.box.IRoBox
import com.gurunars.box.box
import com.gurunars.box.ui.onClick
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import android.os.SystemClock.uptimeMillis
import org.jetbrains.anko.sdk25.coroutines.onTouch


@RunWith(AndroidJUnit4::class)
@LargeTest
class FloatMenuTest {

    lateinit var context: Context
    lateinit var floatMenu: View
    lateinit var content: IRoBox<ContentPane>
    lateinit var menu: IRoBox<MenuPane>
    lateinit var isOpen: IBox<Boolean>
    val clicks = mutableListOf<String>()

    val contentAreaId = View.generateViewId()
    val menuAreaId = View.generateViewId()

    fun getFloatMenu(hasOverlay: Boolean = true): View {
        context = InstrumentationRegistry.getContext()

        content = object : ContentPane {
            override val icon = Icon(icon = R.drawable.ic_menu)
            override fun Context.render() = TextView(context).apply {
                id = contentAreaId
                text = "CONTENT"
                setOnTouchListener { v, event -> clicks.add("CONTENT") }
            }
        }.box

        menu = object : MenuPane {
            override fun Context.render() = TextView(context).apply {
                visibility = View.GONE
                id = menuAreaId
                text = "MENU"
                setOnTouchListener { v, event -> clicks.add("MENU") }
            }

            override val icon = Icon(icon = R.drawable.ic_menu_close)
            override val hasOverlay: Boolean = hasOverlay
        }.box

        isOpen = false.box

        return context.floatMenu(
            contentPane = content,
            menuPane = menu,
            isOpen = isOpen
        ).apply {
            layout(0, 0, 100, 100)
        }
    }

    @After
    fun assertContentVisibility() {
        assertEquals(
            View.VISIBLE,
            floatMenu.findViewById<View>(contentAreaId).visibility
        )
    }

    @Test
    fun closedMenu() {
        floatMenu = getFloatMenu()
        assertEquals(
            View.GONE,
            floatMenu.findViewById<View>(R.id.menuPane).visibility
        )
        assertFalse(isOpen.get())
    }

    @Test
    fun openMenu() {
        floatMenu = getFloatMenu()
        isOpen.set(true)
        assertEquals(
            View.VISIBLE,
            floatMenu.findViewById<View>(R.id.menuPane).visibility
        )
    }

    fun touch() {
        val downTime = SystemClock.uptimeMillis()
        floatMenu.findViewById<View>(R.id.menuPane).onInter(
            MotionEvent.obtain(
                downTime,
                downTime + 600,
                MotionEvent.ACTION_UP,
                50f,
                50f,
                9
            )
        )
    }

    @Test
    fun clicksThroughWithoutOverlayAndOpenMenu() {
        floatMenu = getFloatMenu(false)
        isOpen.set(true)
        touch()
        assertEquals(listOf<String>(), clicks)
    }

    /*
    @Test
    fun clicksThroughWithOverlayAndOpenMenu() {
        floatMenu = getFloatMenu()
        isOpen.set(true)
        floatMenu.findViewById<View>(R.id.menuPane).performClick()
        assertEquals(listOf<String>(), clicks)
    }



    @Test
    fun clicksThroughWithOverlayAndClosedMenu() {
        floatMenu = getFloatMenu()


        assertEquals(listOf("CONTENT"), clicks)
    }
    */

}