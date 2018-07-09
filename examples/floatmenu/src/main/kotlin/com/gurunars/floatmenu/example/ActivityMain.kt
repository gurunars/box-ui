package com.gurunars.floatmenu.example

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import com.gurunars.android_utils.Icon
import com.gurunars.box.*
import com.gurunars.box.ui.*
import com.gurunars.box.ui.layers.layerStack
import com.gurunars.box.ui.layers.statefulLayer
import com.gurunars.floatmenu.contextualMenu
import com.gurunars.floatmenu.floatMenu
import com.gurunars.floatmenu.hasOverlay

class ActivityMain : Activity() {
    private val withOverlay = true.box
    private val isOpen = false.box
    private val notification = "".box
    private val isContextualMenuOpen = false.box

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with<FrameLayout> {
            with<RelativeLayout> {
                fullSize()

                with<TextView> {
                    id = R.id.notificationView
                    padding = OldBounds(dip(15))
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
                    setOnClickListener { notification.set("Content Label Clicked") }
                }.layout(this) {
                    margin = OldBounds(dip(50))
                    alignInParent(HorizontalAlignment.CENTER, VerticalAlignment.CENTER)
                }

                with<TextView> {
                    isOpen.onChange { it -> setText(if (it) R.string.menuOpen else R.string.menuClosed) }
                    isClickable = true
                }.layout(this) {
                    alignWithRespectTo(R.id.textView, verticalPosition = VerticalPosition.BELOW)
                    alignInParent(HorizontalAlignment.CENTER, VerticalAlignment.CENTER)
                }
            }.layout(this) { fullSize() }

            statefulLayer(R.id.main) {
                retain(withOverlay, isOpen, notification)
                layerStack(
                    floatMenu(
                        closeIcon = Icon(
                            bgColor = Color.WHITE,
                            fgColor = Color.BLACK,
                            icon = R.drawable.ic_menu_close
                        ),
                        openIcon = Icon(
                            bgColor = Color.YELLOW,
                            fgColor = Color.BLACK,
                            icon = R.drawable.ic_menu
                        ),
                        isOpen = isOpen
                    ){
                        with<ScrollView> {
                            hasOverlay = withOverlay
                            with<LinearLayout> {
                                orientation = LinearLayout.VERTICAL
                                gravity = Gravity.CENTER_HORIZONTAL
                                with<Button> {
                                    id = R.id.button
                                    setOnClickListener { notification.set("Menu Button Clicked") }
                                    text = getString(R.string.click_me)
                                    padding = OldBounds(dip(10))
                                }.layout(this) {
                                    margin = OldBounds(top=dip(50))
                                }
                                with<FrameLayout> {
                                    id = R.id.buttonFrame
                                    setOnClickListener { notification.set("Menu Button Frame Clicked") }
                                    isClickable = true
                                    setBackgroundColor(Color.MAGENTA)
                                    padding = OldBounds(dip(10))
                                }.layout(this) {
                                    width = dip(100)
                                    height = dip(30)
                                    margin = OldBounds(top=10)
                                }
                            }.layout(this) {
                                asRow()
                            }
                        }
                    },
                    contextualMenu(isOpen=isContextualMenuOpen) {
                        with<ScrollView> {

                        }
                    }
                )
            }.layout(this) { fullSize() }

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
                withOverlay.set(true)
                notification.set("")
                return true
            }
            R.id.toggleBackground -> {
                withOverlay.patch { !this }
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