package com.gurunars.crud_item_list

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.gurunars.item_list.R
import com.gurunars.shortcuts.fullSize


internal fun Context.defaultEmptyViewBinder() =
    TextView(this).apply {
        fullSize()
        text=getString(R.string.empty)
        gravity = Gravity.CENTER
    }
