package com.gurunars.box.ui

import android.util.Log
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
        layoutParams = params
        Log.e("CLS", "" + params.javaClass)
        if (params !is ViewGroup.MarginLayoutParams) return
        Log.e("CLS", "DO")
        params.apply {
            bottomMargin = value.bottom
            topMargin = value.top
            leftMargin = value.left
            rightMargin = value.right
        }
    }

var View.padding: Bounds
    get() {
        return Bounds(
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