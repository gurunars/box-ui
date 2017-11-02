package com.gurunars.crud_item_list

import android.content.Context
import android.view.View
import com.gurunars.databinding.BindableField
import org.jetbrains.anko.frameLayout

internal fun<Key: Enum<Key>> Context.viewSelector(
    vararg viewSelector: Pair<Key, View>,
    selectedView: BindableField<Key>
): View = frameLayout {
    viewSelector.forEach {
        addView(
            it.second.apply {
                tag = it.first
                visibility = View.GONE
            }
        )
    }
    selectedView.onChange(
        beforeChange = {
            findViewWithTag<View>(it)?.visibility = View.GONE
        }
    ) {
        findViewWithTag<View>(it)?.visibility = View.VISIBLE
    }
}