package com.gurunars.box.ui.components.floatmenu

import android.content.Context
import android.view.View
import com.gurunars.box.core.*
import com.gurunars.box.ui.*
import com.gurunars.box.ui.components.Icon
import com.gurunars.box.ui.components.iconView

fun Context.fabDecoratedView(
    fabOnClick: () -> Unit,
    contentView: () -> View
): View = relativeLayout {

    contentView().layoutParams {
        matchParent()
    }

    iconView(icon = ObservableValue(Icon(
        icon = R.drawable.ic_plus
    ))).apply {
        id = R.id.openFab
        onClick(fabOnClick)
    }.layoutParams {
        alignInParent(HorizontalAlignment.RIGHT, VerticalAlignment.BOTTOM)
        width = dip(60)
        height = dip(60)
        margin = Bounds(dip(15))
    }

}