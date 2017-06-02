package com.gurunars.floatmenu.example

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import com.gurunars.floatmenu.FloatMenu
import com.gurunars.floatmenu.Icon
import com.gurunars.floatmenu.floatMenu
import com.gurunars.shortcuts.asRow
import com.gurunars.shortcuts.fullScreen
import com.gurunars.storage.PersistentStorage
import org.jetbrains.anko.*


class MainActivity : AppCompatActivity() {
    private val storage= PersistentStorage(this, "main")

    private val buttonColorFlag = storage.storageField("buttonColorFlag", false)
    private val hasOverlay = storage.storageField("hasOverlay", true)
    private val isLeftHanded = storage.storageField("isLeftHanded", true)

    private lateinit var floatingMenu: FloatMenu

    private fun show(value: String) {
        AlertDialog.Builder(this@MainActivity)
                .setTitle(value)
                .setPositiveButton(android.R.string.yes, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fun color(colorId: Int): Int {
            return ContextCompat.getColor(this, colorId)
        }

        frameLayout {
            fullScreen()
            floatingMenu=floatMenu {
                id=R.id.floatingMenu

                closeIcon.set(Icon(
                    bgColor=color(R.color.White),
                    fgColor=color(R.color.Black),
                    icon=R.drawable.ic_menu_close
                ))

                buttonColorFlag.bind {
                    openIcon.set(Icon(
                        bgColor=color(if (it) R.color.DarkRed else R.color.RosyBrown),
                        fgColor=color(if (it) R.color.White else R.color.Black),
                        icon=R.drawable.ic_menu
                    ))
                }

                hasOverlay.bind(this@MainActivity.hasOverlay)
                isLeftHanded.bind(this@MainActivity.isLeftHanded)

                contentView.set(UI(false) {
                    relativeLayout {
                        fullScreen()
                        textView {
                            id=R.id.textView
                            text=getString(R.string.appName)
                            isClickable=true
                        }.lparams {
                            centerHorizontally()
                            centerVertically()
                        }

                        textView {
                            isOpen.bind { setText(if (it) R.string.menuOpen else R.string.menuClosed) }
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
                        fullScreen()
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
                                backgroundColor=ContextCompat.getColor(context, R.color.AliceBlue)
                                padding=dip(30)
                            }.lparams {
                                topMargin=dip(10)
                            }
                        }.asRow()
                    }
                }.view)
            }.lparams { fullScreen() }
        }

//        storage.load()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        val inflater = menuInflater
        inflater.inflate(R.menu.main_activity, menu)
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