package com.gurunars.example

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import com.gurunars.box.core.ObservableValue
import com.gurunars.box.ui.BaseDeclarativeActivity
import com.gurunars.box.ui.components.floatmenu.contextualFloatMenu
import com.gurunars.box.ui.matchParent
import com.gurunars.box.ui.onClick
import com.gurunars.box.ui.textView

class ActivityFloatMenu : BaseDeclarativeActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val isOpen = ObservableValue(false)

        contextualFloatMenu(
            textView {
                gravity = Gravity.CENTER
                text = "CONTENT"
                onClick {
                    isOpen.set(true)
                }
            },
            textView {
                background =
                    ColorDrawable(Color.GREEN)
                gravity = Gravity.CENTER
                text = "MENU"
            },
            isOpen = isOpen
        ).layoutParams {
            matchParent()
        }
    }

}