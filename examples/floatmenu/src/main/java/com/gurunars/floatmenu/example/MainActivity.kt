package com.gurunars.floatmenu.example

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import com.gurunars.floatmenu.FloatMenu
import com.gurunars.floatmenu.Icon
import com.gurunars.floatmenu.floatMenu
import com.gurunars.test_utils.storage.Assignable
import com.gurunars.test_utils.storage.PersistentStorage
import org.jetbrains.anko.*


class MainActivity : AppCompatActivity() {

    class State : Assignable<State>() {
        var hasOverlay=true
        var isLeftHanded=false
        var flag=true
    }

    private lateinit var floatingMenu: FloatMenu
    private lateinit var storage: PersistentStorage<State>

    private fun toggleButton() {
        floatingMenu.isOpen.set(!floatingMenu.isOpen.get())
    }

    private fun toggleButtonColor() { storage.patch { flag=!flag } }

    private fun toggleBackground() { storage.patch { hasOverlay=!hasOverlay } }

    private fun toggleHand() { storage.patch { isLeftHanded=!isLeftHanded } }

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
            layoutParams=ViewGroup.LayoutParams(matchParent, matchParent)
            floatingMenu=floatMenu {
                id=R.id.floatingMenu

                closeIcon.set(Icon(
                    bgColor=color(R.color.White),
                    fgColor=color(R.color.Black),
                    icon=R.drawable.ic_menu_close
                ))

                hasOverlay.set(true)

                setContentView(UI(false) {
                    relativeLayout {
                        layoutParams=ViewGroup.LayoutParams(matchParent, matchParent)

                        textView {
                            id=R.id.textView
                            text=getString(R.string.appName)
                            isClickable=true
                        }.lparams {
                            centerVertically()
                            centerHorizontally()
                        }

                        textView {
                            isOpen.bind { setText(if (it) R.string.menuOpen else R.string.menuClosed) }
                            isClickable=true
                        }.lparams {
                            below(R.id.textView)
                            centerVertically()
                            centerHorizontally()
                        }

                    }
                }.view)

                setMenuView(UI(false) {
                    scrollView {
                        layoutParams=ViewGroup.LayoutParams(matchParent, matchParent)
                        verticalLayout {
                            gravity=Gravity.CENTER_HORIZONTAL
                            button {
                                setOnClickListener { show("Button Clicked") }
                                text=getString(R.string.click_me)
                                padding=dip(10)
                            }.lparams()
                            frameLayout {
                                setOnClickListener { show("Button Frame Clicked") }
                                isClickable=true
                                backgroundColor=ContextCompat.getColor(context, R.color.AliceBlue)
                                padding=dip(30)
                            }.lparams {
                                topMargin=dip(10)
                            }
                        }.lparams {
                            width=matchParent
                            height=wrapContent
                        }
                    }
                }.view)
            }.lparams {
                width=matchParent
                height=matchParent
            }
        }

        storage = PersistentStorage(this, "FloatMenu", State()) {
            floatingMenu.hasOverlay.set(it.hasOverlay)
            floatingMenu.isLeftHanded.set(it.isLeftHanded)
            floatingMenu.openIcon.set(Icon(
                bgColor=color(if (it.flag) R.color.DarkRed else R.color.RosyBrown),
                fgColor=color(if (it.flag) R.color.White else R.color.Black),
                icon=R.drawable.ic_menu
            ))
        }

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
                toggleBackground()
                return true
            }
            R.id.toggleButtonColor -> {
                toggleButtonColor()
                return true
            }
            R.id.toggleMenu -> {
                toggleButton()
                return true
            }
            R.id.toggleHand -> {
                toggleHand()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

}