package com.gurunars.floatmenu.example

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View.inflate
import android.view.ViewGroup
import android.widget.TextView
import com.gurunars.floatmenu.FloatMenu
import com.gurunars.floatmenu.Icon
import com.gurunars.floatmenu.floatMenu
import com.gurunars.test_utils.storage.Assignable
import com.gurunars.test_utils.storage.PersistentStorage
import org.jetbrains.anko.frameLayout
import org.jetbrains.anko.matchParent


class MainActivity : AppCompatActivity() {

    class State : Assignable<State>() {
        var hasOverlay=true
        var isLeftHanded=false
        var flag=true
    }

    private lateinit var floatingMenu: FloatMenu
    private lateinit var status: TextView
    private lateinit var storage: PersistentStorage<State>

    private fun toggleButton() {
        floatingMenu.isOpen.set(!floatingMenu.isOpen.get())
    }

    private fun toggleButtonColor() { storage.patch { flag=!flag } }

    private fun toggleBackground() { storage.patch { hasOverlay=!hasOverlay } }

    private fun toggleHand() { storage.patch { isLeftHanded=!isLeftHanded } }

    private fun bind(viewId: Int, value: String) {
        findViewById(viewId).setOnClickListener {
            AlertDialog.Builder(this@MainActivity)
                    .setTitle(value)
                    .setPositiveButton(android.R.string.yes, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show()
        }
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

                setContentView(inflate(this@MainActivity, R.layout.content_view, null))
                setMenuView(inflate(this@MainActivity, R.layout.menu_view, null))

                status= findViewById(R.id.status) as TextView
                isOpen.bind { status.setText(if (it) R.string.menuOpen else R.string.menuClosed) }
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

        bind(R.id.textView, "Text Clicked")
        bind(R.id.button, "Button Clicked")
        bind(R.id.buttonFrame, "Button Frame Clicked")

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