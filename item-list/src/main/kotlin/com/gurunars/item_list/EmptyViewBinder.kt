package com.gurunars.item_list

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.gurunars.shortcuts.fullSize

/**
 * View binder for the case when there are no item in the list.
 */
typealias EmptyViewBinder = Context.() -> View

internal fun Context.defaultEmptyViewBinder() =
    TextView(this).apply {
        fullSize()
        text = getString(R.string.empty)
        gravity = Gravity.CENTER
    }
