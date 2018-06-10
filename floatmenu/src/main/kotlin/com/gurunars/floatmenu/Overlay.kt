package com.gurunars.floatmenu

import android.graphics.Color
import android.view.View
import com.gurunars.box.IRoBox

var View.hasOverlay: IRoBox<Boolean>
    get() = throw NotImplementedError()
    set(has) {
        has.onChange {
            setBackgroundColor(if (it) Color.parseColor("#99000000") else Color.TRANSPARENT)
            isClickable = it
        }
    }
