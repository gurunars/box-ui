package com.gurunars.floatmenu.example

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.TextView
import com.gurunars.box.Box
import com.gurunars.box.ui.layoutAsOne
import com.gurunars.box.ui.onClick
import com.gurunars.box.ui.with
import com.gurunars.floatmenu.contextualFabMenu

class ActivityMain : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val isOpen = Box(false)

        contextualFabMenu(
            with<TextView> {
                gravity = Gravity.CENTER
                text = "CONTENT"
                onClick {
                    isOpen.set(true)
                }
            },
            with<TextView> {
                backgroundColor = Color.GREEN
                gravity = Gravity.CENTER
                text = "MENU"
            },
            isOpen = isOpen
        ).layoutAsOne(this)
    }

}