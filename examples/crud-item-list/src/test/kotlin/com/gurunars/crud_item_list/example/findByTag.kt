package com.gurunars.crud_item_list.example

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import org.jetbrains.anko.contentView

fun View.allChildren(): List<View> =
    listOf(this) +
        if (this is ViewGroup)
            (0..childCount).map { getChildAt(it) }
        else
            listOf()

fun View.findByTag(tag: Any) =
    allChildren().filter { it.tag == tag }

fun Activity.findByTag(tag: Any): List<View> =
    contentView?.findByTag(tag) ?: listOf()