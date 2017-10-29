package com.gurunars.item_list

import android.content.Context
import android.view.Gravity
import android.widget.TextView
import com.gurunars.databinding.android.Component
import com.gurunars.databinding.android.fullSize

class DefaultEmptyViewBinder : Component {
    override fun Context.render() = TextView(this).apply {
        id = R.id.noItemsLabel
        fullSize()
        text = getString(R.string.empty)
        gravity = Gravity.CENTER
    }
}
