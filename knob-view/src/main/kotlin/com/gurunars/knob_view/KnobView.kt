package com.gurunars.knob_view

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import com.gurunars.databinding.android.bindableField

class KnobView constructor(
    context: Context,
    viewSelector: Map<Enum<*>, View>
) : FrameLayout(context) {

    class NoSelectorException: Exception()

    val selectedView = bindableField(viewSelector.keys.toList()[0])

    init {
        if (viewSelector.isEmpty()) {
            throw NoSelectorException()
        }
        viewSelector.forEach { type, view -> addView(
            view.apply {
                tag=type
                visibility = View.GONE
            }
        ) }

        selectedView.onChange(
            beforeChange = {
                findViewWithTag(it).visibility = View.GONE
            }
        ) {
            findViewWithTag(it).visibility = View.VISIBLE
        }
    }

}

