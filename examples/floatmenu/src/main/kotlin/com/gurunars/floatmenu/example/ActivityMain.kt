package com.gurunars.floatmenu.example

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import com.gurunars.android_utils.Icon
import com.gurunars.databinding.BindableField
import com.gurunars.databinding.android.asRow
import com.gurunars.databinding.android.fullSize
import com.gurunars.databinding.android.setAsOne
import com.gurunars.databinding.field
import com.gurunars.databinding.patch
import com.gurunars.floatmenu.floatMenu
import com.gurunars.storage.PersistentStorage
import org.jetbrains.anko.*

class ActivityMain : Activity() {
    private val storage = PersistentStorage(this, "main")

    private val buttonColorFlag = storage.storageField("buttonColorFlag", false)
    private val hasOverlay = storage.storageField("hasOverlay", true)
    private val isLeftHanded = storage.storageField("isLeftHanded", false)

    private val isOpen = BindableField(false)
    private val notification = BindableField("")

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
                    isOpen.onChange { setText(if (it) R.string.menuOpen else R.string.menuClosed) }
                    isClickable = true
                }.lparams {
                    below(R.id.textView)
                    centerHorizontally()
                    centerVertically()
                }
            }
        }.view

        val openIcon = Icon(icon = R.drawable.ic_menu).field

        buttonColorFlag.onChange {
            openIcon.set(Icon(
                bgColor = if (it) Color.RED else Color.YELLOW,
                fgColor = if (it) Color.WHITE else Color.BLACK,
                icon = R.drawable.ic_menu
            ))
        }

        floatMenu(
            contentView.field,
            menuView.field,
            isOpen = isOpen,
            closeIcon = Icon(
                bgColor = Color.WHITE,
                fgColor = Color.BLACK,
                icon = R.drawable.ic_menu_close
            ).field,
            hasOverlay = hasOverlay,
            isLeftHanded = isLeftHanded,
            openIcon = openIcon
        ).setAsOne(this)

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
                isOpen.set(false)
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
                isOpen.patch { !this }
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