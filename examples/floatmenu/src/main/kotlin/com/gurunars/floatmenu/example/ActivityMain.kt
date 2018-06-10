package com.gurunars.floatmenu.example

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import com.gurunars.android_utils.Icon
import com.gurunars.box.*
import com.gurunars.box.ui.*
import com.gurunars.floatmenu.ContentPane
import com.gurunars.floatmenu.MenuPane
import com.gurunars.floatmenu.floatMenu

class ActivityMain : Activity() {
    private val hasOverlay = true.box
    private val isOpen = false.box
    private val notification = "".box

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val menuView = with<ScrollView> {
            fullSize()
            with<LinearLayout> {
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.CENTER_HORIZONTAL
                with<Button> {
                    id = R.id.button
                    setOnClickListener { notification.set("Menu Button Clicked") }
                    text = getString(R.string.click_me)
                    padding = Bounds(dip(10))
                }.layout(this) {
                    margin = Bounds(top=dip(50))
                }
                with<FrameLayout> {
                    id = R.id.buttonFrame
                    setOnClickListener { notification.set("Menu Button Frame Clicked") }
                    isClickable = true
                    setBackgroundColor(Color.MAGENTA)
                    padding = Bounds(dip(10))
                }.layout(this) {
                    margin = Bounds(top=10)
                }
            }.layout(this) {
                asRow()
            }
        }

        val contentView = with<RelativeLayout> {
            fullSize()

            with<TextView> {
                id = R.id.notificationView
                padding = Bounds(dip(15))
                gravity = Gravity.CENTER
                setBackgroundColor(Color.parseColor("#FFFFAA"))

                notification.onChange { it -> text = it }

                setOnLongClickListener {
                    text = ""
                    true
                }
            }.layout(this) {
                asRow()
                alignInParent(HorizontalAlignment.CENTER, VerticalAlignment.TOP)
            }

            with<TextView> {
                id = R.id.textView
                text = getString(R.string.appName)
                isClickable = true
                setOnClickListener { notification.set("Content Text Clicked") }
            }.layout(this) {
                margin = Bounds(dip(50))
                alignInParent(HorizontalAlignment.CENTER, VerticalAlignment.CENTER)
            }

            with<TextView> {
                isOpen.onChange { it -> setText(if (it) R.string.menuOpen else R.string.menuClosed) }
                isClickable = true
            }.layout(this) {
                alignWithRespectTo(R.id.textView, verticalPosition = VerticalPosition.BELOW)
                alignInParent(HorizontalAlignment.CENTER, VerticalAlignment.CENTER)
            }
        }

        val contentArea: IBox<ContentPane> = object : ContentPane {
            override fun Context.render() = contentView
            override val icon = Icon(
                bgColor = Color.YELLOW,
                fgColor = Color.BLACK,
                icon = R.drawable.ic_menu
            )
        }.box

        class MenuArea(override val hasOverlay: Boolean) : MenuPane {
            override fun Context.render() = menuView
            override val icon = Icon(
                bgColor = Color.WHITE,
                fgColor = Color.BLACK,
                icon = R.drawable.ic_menu_close
            )
        }

        statefulView(R.id.main) {
            retain(hasOverlay, isOpen, notification)
            floatMenu(
                contentArea,
                hasOverlay.oneWayBranch { MenuArea(this) },
                isOpen = isOpen
            ).layoutAsOne(this)
        }.layoutAsOne(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main_activity, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.reset -> {
                isOpen.set(false)
                hasOverlay.set(true)
                notification.set("")
                return true
            }
            R.id.toggleBackground -> {
                hasOverlay.patch { !this }
                return true
            }
            R.id.toggleMenu -> {
                isOpen.patch { !this }
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}