package com.gurunars.floatmenu.example

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import com.gurunars.android_utils.IconView
import com.gurunars.databinding.BindableField
import com.gurunars.floatmenu.FloatMenu
import com.gurunars.shortcuts.asRow
import com.gurunars.shortcuts.fullSize
import com.gurunars.shortcuts.setAsOne
import com.gurunars.storage.PersistentStorage
import org.jetbrains.anko.*

class ActivityMain : Activity() {
    private val storage = PersistentStorage(this, "main")

    private val buttonColorFlag = storage.storageField("buttonColorFlag", false)
    private val hasOverlay = storage.storageField("hasOverlay", true)
    private val isLeftHanded = storage.storageField("isLeftHanded", false)

    private val notification = BindableField("")

    private lateinit var floatingMenu: FloatMenu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val isOpenT = BindableField(false)

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

                    notification.onChange {
                        text = it
                    }

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
                    isOpenT.onChange { setText(if (it) R.string.menuOpen else R.string.menuClosed) }
                    isClickable = true
                }.lparams {
                    below(R.id.textView)
                    centerHorizontally()
                    centerVertically()
                }
            }
        }.view

        floatingMenu = FloatMenu(this, contentView, menuView).setAsOne(this) {
            fullSize()
            id = R.id.floatingMenu

            isOpen.bind(isOpenT)

            closeIcon.set(IconView.Icon(
                bgColor = Color.WHITE,
                fgColor = Color.BLACK,
                icon = R.drawable.ic_menu_close
            ))

            buttonColorFlag.onChange {
                openIcon.set(IconView.Icon(
                    bgColor = if (it) Color.RED else Color.YELLOW,
                    fgColor = if (it) Color.WHITE else Color.BLACK,
                    icon = R.drawable.ic_menu
                ))
            }

            this@ActivityMain.hasOverlay.bind(hasOverlay)
            this@ActivityMain.isLeftHanded.bind(isLeftHanded)
        }

        storage.load()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        storage.unbindAll()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.main_activity, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.reset -> {
                isLeftHanded.set(false)
                floatingMenu.isOpen.set(false)
                buttonColorFlag.set(false)
                isLeftHanded.set(false)
                hasOverlay.set(true)
                return true
            }
            R.id.toggleBackground -> {
                hasOverlay.set(!hasOverlay.get())
                return true
            }
            R.id.toggleButtonColor -> {
                buttonColorFlag.set(!buttonColorFlag.get())
                return true
            }
            R.id.toggleMenu -> {
                floatingMenu.isOpen.set(!floatingMenu.isOpen.get())
                return true
            }
            R.id.toggleHand -> {
                isLeftHanded.set(!isLeftHanded.get())
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

}