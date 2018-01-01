package com.gurunars.floatmenu.example

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import com.gurunars.android_utils.Icon
import com.gurunars.databinding.IBox
import com.gurunars.databinding.android.asRow
import com.gurunars.databinding.android.fullSize
import com.gurunars.databinding.android.setAsOne
import com.gurunars.databinding.android.statefulView
import com.gurunars.databinding.box
import com.gurunars.databinding.branch
import com.gurunars.databinding.patch
import com.gurunars.floatmenu.ContentPane
import com.gurunars.floatmenu.MenuPane
import com.gurunars.floatmenu.floatMenu
import org.jetbrains.anko.UI
import org.jetbrains.anko.alignParentTop
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.below
import org.jetbrains.anko.button
import org.jetbrains.anko.centerHorizontally
import org.jetbrains.anko.centerVertically
import org.jetbrains.anko.dip
import org.jetbrains.anko.frameLayout
import org.jetbrains.anko.padding
import org.jetbrains.anko.relativeLayout
import org.jetbrains.anko.scrollView
import org.jetbrains.anko.textView
import org.jetbrains.anko.verticalLayout

class ActivityMain : Activity() {
    private val hasOverlay = true.box
    private val isOpen = false.box
    private val notification = "".box

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val menuView = UI(false) {
            scrollView {
                fullSize()
                verticalLayout {
                    gravity = Gravity.CENTER_HORIZONTAL
                    button {
                        id = R.id.button
                        setOnClickListener { notification.set("Menu Button Clicked") }
                        text = getString(R.string.click_me)
                        padding = dip(10)
                    }.lparams {
                        topMargin = dip(50)
                    }
                    frameLayout {
                        id = R.id.buttonFrame
                        setOnClickListener { notification.set("Menu Button Frame Clicked") }
                        isClickable = true
                        backgroundColor = Color.MAGENTA
                        padding = dip(30)
                    }.lparams {
                        topMargin = dip(10)
                    }
                }.asRow()
            }
        }.view

        val contentView = UI(false) {
            relativeLayout {
                fullSize()

                textView {
                    id = R.id.notificationView
                    padding = dip(15)
                    gravity = Gravity.CENTER
                    backgroundColor = Color.parseColor("#FFFFAA")

                    notification.onChange { it -> text = it }

                    setOnLongClickListener {
                        text = ""
                        true
                    }
                }.lparams {
                    asRow()
                    centerHorizontally()
                    alignParentTop()
                }

                textView {
                    id = R.id.textView
                    text = getString(R.string.appName)
                    isClickable = true
                    setOnClickListener { notification.set("Content Text Clicked") }
                }.lparams {
                    bottomMargin = dip(50)
                    centerHorizontally()
                    centerVertically()
                }

                textView {
                    isOpen.onChange { it -> setText(if (it) R.string.menuOpen else R.string.menuClosed) }
                    isClickable = true
                }.lparams {
                    below(R.id.textView)
                    centerHorizontally()
                    centerVertically()
                }
            }
        }.view

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
                hasOverlay.branch { MenuArea(this) },
                isOpen = isOpen
            ).setAsOne(this)
        }.setAsOne(this)

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
                hasOverlay.set(!hasOverlay.get())
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