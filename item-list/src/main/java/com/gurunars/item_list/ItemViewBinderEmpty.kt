package com.gurunars.item_list

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

internal class ItemViewBinderEmpty : EmptyViewBinder {

    override fun getView(context: Context): View {
        return TextView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT)
            setText(R.string.empty)
            gravity = Gravity.CENTER
        }
    }

    companion object {

        val EMPTY_TYPE = -404
    }

}
