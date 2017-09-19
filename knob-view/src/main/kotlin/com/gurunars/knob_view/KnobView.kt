package com.gurunars.knob_view

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import com.gurunars.databinding.android.bindableField

/**
 * Meta view that displays one of the views listed in a viewSelector.
 *
 * @param viewSelector a mapping between an enum identifier and a view to be shown
 *
 * @property selectedView an enum field matching the view to be shown at a given moment
 */
class KnobView constructor(
    context: Context,
    viewSelector: Map<Enum<*>, View>
) : FrameLayout(context) {

    val selectedView = bindableField(viewSelector.keys.toList()[0])

    init {
        viewSelector.forEach { type, view -> addView(
            view.apply {
                tag=type
                visibility = View.GONE
            }
        ) }

        selectedView.onChange(
            beforeChange = {
                findViewWithTag(it)?.visibility = View.GONE
            }
        ) {
            findViewWithTag(it)?.visibility = View.VISIBLE
        }
    }

}

