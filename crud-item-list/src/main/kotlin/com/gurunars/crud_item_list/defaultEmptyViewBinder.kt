package com.gurunars.crud_item_list

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.gurunars.item_list.R
import com.gurunars.shortcuts.fullSize


internal fun defaultEmptyViewBinder(context: Context): View {
    return TextView(context).apply {
        fullSize()
        setText(R.string.empty)
        gravity = Gravity.CENTER
    }
}