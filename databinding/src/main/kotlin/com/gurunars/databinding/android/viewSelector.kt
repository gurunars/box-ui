package com.gurunars.databinding.android

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import com.gurunars.databinding.BindableField

fun<Key: Enum<Key>> Context.viewSelector(
    vararg viewSelector: Pair<Key, View?>,
    selectedView: BindableField<Key>
): View = FrameLayout(this).apply {
    val mapping = viewSelector.toMap()
    selectedView.onChange{ type ->
        mapping[type]?.setAsOne(this) {
            fullSize()
        }
    }
}