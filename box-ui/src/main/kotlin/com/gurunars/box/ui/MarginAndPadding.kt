package com.gurunars.box.ui

import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT

var View.margin: Bounds
    get() {
        val params = layoutParams
        params as ViewGroup.MarginLayoutParams
        return Bounds(
            left = params.leftMargin,
            right = params.rightMargin,
            top = params.topMargin,
            bottom = params.bottomMargin
        )
    }
    set(value) {
        val params = layoutParams ?: ViewGroup.MarginLayoutParams(
            WRAP_CONTENT,
            WRAP_CONTENT
        )
        params as ViewGroup.MarginLayoutParams
        params.apply {
            bottomMargin = value.bottom
            topMargin = value.top
            leftMargin = value.left
            rightMargin = value.right
        }
    }