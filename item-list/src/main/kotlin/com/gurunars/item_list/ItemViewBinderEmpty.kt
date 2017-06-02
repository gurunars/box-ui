package com.gurunars.item_list

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import org.jetbrains.anko.matchParent

internal class ItemViewBinderEmpty : EmptyViewBinder {

    override fun getView(context: Context): View {
        return TextView(context).apply {
            layoutParams = ViewGroup.LayoutParams(matchParent, matchParent)
            setText(R.string.empty)
            gravity = Gravity.CENTER
        }
    }

    companion object {

        val EMPTY_TYPE = -404
    }

}
