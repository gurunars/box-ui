package com.gurunars.example

import android.os.Bundle
import android.view.Gravity
import com.gurunars.box.ui.BaseDeclarativeActivity
import com.gurunars.box.ui.components.editPinCode
import com.gurunars.box.ui.frameLayout
import com.gurunars.box.ui.matchParent
import com.gurunars.box.ui.wrapContent

class ActivityEditPinCode: BaseDeclarativeActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        frameLayout {

            editPinCode {
                pinLength = 5
                charsVisible = true
            }.layoutParams {
                wrapContent()
                gravity = Gravity.CENTER
            }

        }.layoutParams {
            matchParent()
        }

    }

}