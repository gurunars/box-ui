package com.gurunars.box.ui

import android.view.View
import android.view.ViewGroup

var ViewGroup.MarginLayoutParams.margin: OldBounds
    get() = OldBounds(
        left = leftMargin,
        right = rightMargin,
        top = topMargin,
        bottom = bottomMargin
    )
    set(value) {
        bottomMargin = value.bottom
        topMargin = value.top
        leftMargin = value.left
        rightMargin = value.right
    }

var View.padding: OldBounds
    get() {
        return OldBounds(
            left = paddingLeft,
            right = paddingRight,
            top = paddingTop,
            bottom = paddingBottom
        )
    }
    set(value) {
        setPadding(
            value.left,
            value.top,
            value.right,
            value.bottom
        )
    }