package com.gurunars.floatmenu.example

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import com.gurunars.android_utils.IconView
import com.gurunars.floatmenu.FloatMenu
import com.gurunars.floatmenu.floatMenu
import com.gurunars.shortcuts.asRow
import com.gurunars.shortcuts.color
import com.gurunars.shortcuts.fullSize
import com.gurunars.storage.PersistentStorage
import org.jetbrains.anko.*


class ActivityMain : Activity() {
    private val storage=PersistentStorage(this, "main")

    private val buttonColorFlag = storage.storageField("buttonColorFlag", false)
    private val hasOverlay = storage.storageField("hasOverlay", true)
    private val isLeftHanded = storage.storageField("isLeftHanded", true)

    private lateinit var floatingMenu: FloatMenu

    private fun show(value: String) {
        AlertDialog.Builder(this@ActivityMain)
                .setTitle(value)
                .setPositiveButton(android.R.string.yes, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        floatingMenu=floatMenu {
            fullSize()
            id=R.id.floatingMenu

            closeIcon.set(IconView.Icon(
                bgColor = color(R.color.White),
                fgColor = color(R.color.Black),
                icon = R.drawable.ic_menu_close
            ))

            buttonColorFlag.onChange {
                openIcon.set(IconView.Icon(
                    bgColor = color(if (it) R.color.DarkRed else R.color.RosyBrown),
                    fgColor = color(if (it) R.color.White else R.color.Black),
                    icon = R.drawable.ic_menu
                ))
            }

            this@ActivityMain.hasOverlay.bind(hasOverlay)
            this@ActivityMain.isLeftHanded.bind(isLeftHanded)

            contentView.set(UI(false) {
                relativeLayout {
                    fullSize()
                    textView {
                        id=R.id.textView
                        text=getString(R.string.appName)
                        isClickable=true
                    }.lparams {
                        centerHorizontally()
                        centerVertically()
                    }

                    textView {
                        isOpen.onChange { setText(if (it) R.string.menuOpen else R.string.menuClosed) }
                        isClickable=true
                    }.lparams {
                        below(R.id.textView)
                        centerHorizontally()
                        centerVertically()
                    }
                }
            }.view)

            menuView.set(UI(false) {
                scrollView {
                    fullSize()
                    verticalLayout {
                        gravity=Gravity.CENTER_HORIZONTAL
                        button {
                            id=R.id.button
                            setOnClickListener { show("Button Clicked") }
                            text=getString(R.string.click_me)
                            padding=dip(10)
                        }.lparams()
                        frameLayout {
                            id=R.id.buttonFrame
                            setOnClickListener { show("Button Frame Clicked") }
                            isClickable=true
                            backgroundColor=color(R.color.AliceBlue)
                            padding=dip(30)
                        }.lparams {
                            topMargin=dip(10)
                        }
                    }.asRow()
                }
            }.view)
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